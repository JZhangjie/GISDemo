package com.demo.jzhangjie.gisdemo.components.map;

import android.location.Location;

import com.demo.jzhangjie.gisdemo.constant.ApplicationConstant;
import com.demo.jzhangjie.gisdemo.data.model.layer.TianDiTuTiledMapServiceLayer;
import com.demo.jzhangjie.gisdemo.utils.FileHelper;
import com.esri.android.map.Layer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.core.geometry.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/20.
 */

public class MapPresenter implements MapContract.Presenter {
    private List<Layer> mLayerList;
    private MapContract.View mView;
    private boolean firstLoad;

    public MapPresenter(MapContract.View view){
        this.firstLoad=true;
        this.mView=view;
        this.mView.setPresenter(this);
    }

    @Override
    public void start() {
        load(firstLoad);
        firstLoad=false;
    }

    public void load(boolean create){
        loadLayers();
    }

    private void loadLayers(){
        String path = FileHelper.getRootPath() + "/"+ ApplicationConstant.LXYXPATH;
        ArcGISLocalTiledLayer mLXYXLayer = new ArcGISLocalTiledLayer(path);
        TianDiTuTiledMapServiceLayer mZXYXLayer = new TianDiTuTiledMapServiceLayer.Builder()
                .setUrl(ApplicationConstant.ZXYXURL)
                .setLayerId("img")
                .setStyle("default")
                .setFormat("tiles")
                .setTileMatrixSet("c").builder();
        TianDiTuTiledMapServiceLayer mZXSLLayer = new TianDiTuTiledMapServiceLayer.Builder()
                .setUrl(ApplicationConstant.ZXSLURL)
                .setLayerId("vec")
                .setStyle("default")
                .setFormat("tiles")
                .setTileMatrixSet("c").builder();;
        this.addLayer(mZXYXLayer);
        this.addLayer(mZXSLLayer);
        this.addLayer(mLXYXLayer);
        this.setLayerVisible(1,false);
    }

    @Override
    public int addLayer(Layer layer) {
        if(this.mLayerList==null)
            this.mLayerList = new ArrayList<>();
        if(this.mLayerList.contains(layer))
            return -1;
        int index = this.mLayerList.size();
        this.mLayerList.add(layer);
        this.mView.addLayer(layer,index);
        return index;
    }

    @Override
    public int removeLayer(Layer layer) {
        return 0;
    }

    @Override
    public void setLayerVisible(int layerindex, boolean isshow) {
        Layer layer = mLayerList.get(layerindex);
        layer.setVisible(isshow);
    }

    @Override
    public Layer getLayer(int index) {
        return mLayerList.get(index);
    }

    @Override
    public void zoomTo(Location location) {

    }

    @Override
    public void zoomTo(Geometry geometry) {

    }
}
