package com.demo.jzhangjie.gisdemo.components.map;

import android.location.Location;

import com.demo.jzhangjie.gisdemo.BasePresenter;
import com.demo.jzhangjie.gisdemo.BaseView;
import com.esri.android.map.Layer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

/**
 * Created by admin on 2017/9/20.
 */

public interface MapContract {

    public interface View extends BaseView<Presenter>{

        int addLayer(Layer layer,int index);

        int removeLayer(Layer layer);

        int removeLayer(int index);

        void zoomTo(Point point);

        void zoomTo(Geometry geometry);

    }

    public interface Presenter extends BasePresenter{

        int addLayer(Layer layer);

        int removeLayer(Layer layer);

        void setLayerVisible(int layerindex,boolean isshow);

        Layer getLayer(int index);

        void zoomTo(Location location);

        void zoomTo(Geometry geometry);


    }

}
