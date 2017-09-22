package com.demo.jzhangjie.gisdemo.components.map;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.demo.jzhangjie.gisdemo.R;
import com.esri.android.map.Layer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements MapContract.View,View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private MapView mMapView;
    private View mLayerControlButton;
    private FloatingActionButton mLocationButton;
    private LocationDisplayManager mLocationManager;
    private PopupWindow mLayerControlPopupWindow;
    private ToggleButton mLayerControlRbtLXYX;
    private RadioButton mLayerControlRbtZXSL;
    private RadioButton mLayerControlRbtZXYX;

    private MapContract.Presenter mPresenter;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView)view.findViewById(R.id.map_map);
        mLayerControlButton = view.findViewById(R.id.map_btn_layercontrol);
        mLocationButton = (FloatingActionButton)view.findViewById(R.id.map_btn_location);
        View v = inflater.inflate(R.layout.control_layercontrol_popupwindow,null);
        mLayerControlPopupWindow = new PopupWindow(v, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT,true);
        mLayerControlRbtLXYX = (ToggleButton)v.findViewById(R.id.layercontrol_toggle_lx_yx);
        mLayerControlRbtZXSL=(RadioButton) v.findViewById(R.id.layercontrol_toggle_zx_sl);
        mLayerControlRbtZXYX=(RadioButton) v.findViewById(R.id.layercontrol_toggle_zx_yx);

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(o==mMapView && status==STATUS.INITIALIZED) {
                    mLocationManager = mMapView.getLocationDisplayManager();
                }
            }
        });
        mLocationButton.setOnClickListener(this);
        mLayerControlButton.setOnClickListener(this);
        mLayerControlRbtLXYX.setOnCheckedChangeListener(this);
        mLayerControlRbtZXSL.setOnCheckedChangeListener(this);
        mLayerControlRbtZXYX.setOnCheckedChangeListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.mPresenter!=null)
            this.mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public int addLayer(Layer layer, int index) {
        mMapView.addLayer(layer,index);
        return index;
    }

    @Override
    public int removeLayer(Layer layer) {
        mMapView.removeLayer(layer);
        return 0;
    }

    @Override
    public int removeLayer(int index) {
        mMapView.removeLayer(index);
        return 0;
    }

    @Override
    public void zoomTo(Point point) {
        mMapView.centerAt(point,true);
    }

    @Override
    public void zoomTo(Geometry geometry) {
        mMapView.setExtent(geometry);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map_btn_location:
                if(mLocationManager!=null && !mLocationManager.isStarted())
                    mLocationManager.start();
                break;
            case R.id.map_btn_layercontrol:
                mLayerControlPopupWindow.showAsDropDown(v);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.layercontrol_toggle_lx_yx:
                mPresenter.setLayerVisible(2,isChecked);
                break;
            case R.id.layercontrol_toggle_zx_sl:
                mPresenter.setLayerVisible(1,isChecked);
                break;
            case R.id.layercontrol_toggle_zx_yx:
                mPresenter.setLayerVisible(0,isChecked);
                break;
            default:
                break;
        }
    }
}
