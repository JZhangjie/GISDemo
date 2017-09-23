package com.demo.jzhangjie.gisdemo.data.model.esri;

/**
 * Created by admin on 2017/9/23.
 */

public class DBField {
    private String cid;
    private String name;
    private Class type;
    private String oname;
    private String notnull;
    private String dflt_value;
    private String pk;

    public String getOname() {
        return oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getNotnull() {
        return notnull;
    }

    public void setNotnull(String notnull) {
        this.notnull = notnull;
    }

    public String getDflt_value() {
        return dflt_value;
    }

    public void setDflt_value(String dflt_value) {
        this.dflt_value = dflt_value;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public DBField(String name, Class type) {
        this.name = name;
        this.type = type;
    }

    public DBField(String name, Class type,String oname) {
        this.name = name;
        this.type = type;
        this.oname = oname;
    }

    public DBField(String cid, String name, Class type) {
        this.cid = cid;
        this.name = name;
        this.type = type;
    }

    public DBField(String cid, String name, Class type, String notnull, String dflt_value, String pk) {
        this.cid = cid;
        this.name = name;
        this.type = type;
        this.notnull = notnull;
        this.dflt_value = dflt_value;
        this.pk = pk;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DBField){
            DBField temp = (DBField)obj;
            return this.getName().equals(temp.getName());
        }
        return false;
    }
}
