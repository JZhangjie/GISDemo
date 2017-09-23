package com.demo.jzhangjie.gisdemo.data.model.esri;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.Symbol;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by admin on 2017/9/22.
 *持有db文件存储的图斑的几何信息和属性信息；
 * 需要一个继承自DBGraphicsLayer.DBFeature的图斑类；
 * 提供文件路径，表名，几何字段名，确定符号的字段名，唯一标识的字段名
 */

public class DBGraphicsLayer<T extends DBGraphicsLayer.DBFeature> extends GraphicsLayer {
    private String mDBFilePath;
    private String mTableName;
    private DBField mGeometryField;
    private DBField mSymbolField;
    private DBField mUniqueField;
    private List<T> mFeatures;
    private Class<T> entityClass;
    private List<DBField> mDBFields;
    private List<DBField> mSelectFields;
    private List<DBField> mUpdateFields;
    private Map<Object,Symbol> mSymbolList;
    private Symbol mDefaultSymbol;

    public DBGraphicsLayer(String dbfilePath, Class<T> type) throws Exception {
        super();
        init(dbfilePath, "gddc", "Shape", "HCZT", "FID",new HashMap<Object,Symbol>(), type);
    }

    public DBGraphicsLayer(GraphicsLayer.RenderingMode mode, String dbfilePath, Class<T> type) throws Exception {
        super(mode);
        init(dbfilePath, "gddc", "Shape",  "HCZT", "FID",new HashMap<Object,Symbol>(), type);
    }

    public DBGraphicsLayer(GraphicsLayer.RenderingMode mode, String dbfilePath, String tablename, String geometryColumnName, String symbolColumnName, String uniqueColumnName,Map<Object,Symbol> symbolList, Class<T> type) throws Exception {
        super(mode);
        init(dbfilePath, tablename, geometryColumnName, symbolColumnName, uniqueColumnName,symbolList, type);
    }

    public DBGraphicsLayer(SpatialReference sr, Envelope fullextent, GraphicsLayer.RenderingMode mode, String dbfilePath, String tablename, String geometryColumnName, String symbolColumnName, String uniqueColumnName,Map<Object,Symbol> symbolList,Class<T> type) throws Exception {
        super(sr, fullextent, mode);
        init(dbfilePath, tablename, geometryColumnName, symbolColumnName, uniqueColumnName,symbolList, type);
    }

    /**
     *
     * @param dbfilePath db文件路径
     * @param tablename  db文件中使用的表名
     * @param geometryColumnName  存储几何数据的字段名称
     * @param symbolColumnName    进行符号分类的字段名称
     * @param uniqueColumnName    唯一表示图斑的字段名称
     * @param symbolList           符号列表              "defaultsymbol"
     * @param type                  图斑类
     */
    private void init(String dbfilePath, String tablename, String geometryColumnName, String symbolColumnName, String uniqueColumnName,Map<Object,Symbol> symbolList, Class<T> type) {
        File file = new File(dbfilePath);
        boolean a = file.exists();
        boolean b = dbfilePath.endsWith(".db");
        if (!file.exists() || !dbfilePath.endsWith(".db"))
            throw new RuntimeException("文件不存在，或格式不正确！");
        this.mDBFilePath = dbfilePath;
        this.mTableName = tablename;
        this.entityClass = type;
        this.mSymbolList = new HashMap<Object,Symbol>();
        for(Map.Entry<Object,Symbol> entry :symbolList.entrySet()){
            this.mSymbolList.put(entry.getKey(),entry.getValue());
        }
        //获取db表结构
        String sql = "pragma table_info(" + this.mTableName + ")";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(this.mDBFilePath, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = database.rawQuery(sql, null);
        this.mDBFields = new ArrayList<>();
        while (cursor.moveToNext()) {
            String cid = cursor.getString(cursor.getColumnIndex("cid"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String temptype = cursor.getString(cursor.getColumnIndex("type"));
            DBField field = new DBField(cid, name, toJavaClass(temptype));
            this.mDBFields.add(field);
            if (name.equals(geometryColumnName)) {
                this.mGeometryField = field;
            }
            if (name.equals(symbolColumnName)) {
                this.mSymbolField = field;
            }
            if (name.equals(uniqueColumnName)) {
                this.mUniqueField = field;
            }
        }
        cursor.close();
        database.close();
        if (this.mDBFields.size() <= 0) {
            throw new RuntimeException("数据库或表不正确！");
        }
        if (this.mGeometryField == null || this.mUniqueField == null) {
            throw new RuntimeException("几何或标识字段名称不正确或不存在！");
        }
        initFeatureFields();
    }

    /**
     * 初始化字段列表，从泛型对象获取的用于查询、更新的字段列表
    */
    private void initFeatureFields() {
        this.mUpdateFields = new ArrayList<>();
        this.mSelectFields = new ArrayList<>();
        Field[] fields = this.entityClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                if (column.ColumnName() != null && column.ColumnType() != null) {
                    DBField dbField = new DBField(column.ColumnName(), column.ColumnType(),field.getName());
                    if (!this.mDBFields.contains(dbField)) {
                        throw new RuntimeException(column.ColumnName() + ":对应字段不存在!");
                    }
                    this.mSelectFields.add(dbField);
                    if (column.AccessMode() == ColumnAccessMode.ReadAndWrite) {
                        this.mUpdateFields.add(dbField);
                    }
                }
            }
        }
    }

    /**
     * 从db加载图斑
     * @return 加载成功返回加载图斑个数，加载失败返回-1
     */
    public int load() {
        this.mFeatures = new ArrayList<>();
        SQLiteDatabase database = SQLiteDatabase.openDatabase(this.mDBFilePath, null, SQLiteDatabase.OPEN_READWRITE);
        String sql = this.getSelectSQL();
        Cursor cursor = database.rawQuery(sql, null);
        int num = 0;
        while (cursor.moveToNext()) {
            try {
                T feature = entityClass.newInstance();
                for (int i = 0; i < this.mSelectFields.size(); i++) {
                    DBField field = this.mSelectFields.get(i);
                    setFeatureFieldValue(feature,field,cursor,field.getName());
                }
                String shape = cursor.getString(cursor.getColumnIndex(this.mGeometryField.getName()));
                JsonFactory f = new JsonFactory();
                JsonParser jp = f.createJsonParser(shape);
                Geometry p = (Geometry) GeometryEngine.jsonToGeometry(jp).getGeometry();
                Graphic graphic = new Graphic(p,getSymbol(feature));
                int id = this.addGraphic(graphic);
                if(id!=-1){
                    Graphic newgraphic = this.getGraphic(id);
                    feature.setGraphic(newgraphic);
                    this.mFeatures.add(feature);
                    num++;
                }

            } catch (Exception e) {
                return -1;
            }
        }
        cursor.close();
        database.close();
        return num;
    }

    private String getSelectSQL(){
        String sql = "select ";
        for(DBField dbField:this.mSelectFields){
            sql +=dbField.getName() +",";
        }
        sql+=this.mGeometryField.getName();
        sql+=" from "+this.mTableName ;
        return sql;
    }

    private Symbol getSymbol(T feature) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Symbol symbol=null;
        if(this.mSymbolList!=null){
             symbol = this.mSymbolList.get(getFeatureFieldValue(feature,this.mSymbolField));
        }
        if(symbol==null)
            symbol= new SimpleFillSymbol(Color.RED, SimpleFillSymbol.STYLE.CROSS);
        return symbol;
    }

    //依据字段名称和类型，将db的cursor的index位置的值赋给对应字段
    private void setFeatureFieldValue(T feature, DBField field,Cursor cursor,String columnName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String first = field.getOname().substring(0, 1).toUpperCase();
        String setter = "set" + first + field.getOname().substring(1);

        if(field.getType() == Integer.class){
            Integer value = cursor.getInt(cursor.getColumnIndex(columnName));
            Method method = entityClass.getMethod(setter,Integer.class);
            method.invoke(feature,value);
            return;
        }
        if(field.getType() == Double.class){
            double value = cursor.getDouble(cursor.getColumnIndex(columnName));
            Method method = entityClass.getMethod(setter,Double.class);
            method.invoke(feature,value);
            return;
        }
        String value = cursor.getString(cursor.getColumnIndex(columnName));
        Method method = entityClass.getMethod(setter, String.class);
        method.invoke(feature,value);
    }

    //根据字段获取字段值
    private Object getFeatureFieldValue(T feature, DBField field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String first = field.getOname().substring(0, 1).toUpperCase();
        String getter = "get" + first + field.getOname().substring(1);
        Method method = entityClass.getMethod(getter,new Class[]{});
        return  method.invoke(feature,new Object[]{});
    }

    public T getSelectedFeature(float x,float y,int tolerance){
        int[] ids = this.getGraphicIDs(x,y,tolerance,1);
        if(ids!=null & ids.length>0) {
            Graphic graphic = this.getGraphic(ids[0]);
            for(T t:this.mFeatures){
                if(t.getGraphic().getUid()==graphic.getUid()){
                    return t;
                }
            }
        }
        return null;
    }

    public static Class toJavaClass(String typename) {
        switch (typename.trim().toLowerCase()) {
            case "integer":
            case "int":
                return Integer.class;
            case "real":
            case "numeric":
                return Double.class;
            case "boolean":
            case "bool":
                return Boolean.class;
            default:
                return String.class;
        }
    }

    public static abstract class DBFeature {
        private Graphic graphic;

        public Graphic getGraphic() {
            return graphic;
        }

        protected void setGraphic(Graphic graphic) {
            this.graphic = graphic;
        }

    }
}
