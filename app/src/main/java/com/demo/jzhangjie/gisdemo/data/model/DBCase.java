package com.demo.jzhangjie.gisdemo.data.model;

import com.demo.jzhangjie.gisdemo.data.model.esri.Column;
import com.demo.jzhangjie.gisdemo.data.model.esri.ColumnAccessMode;
import com.demo.jzhangjie.gisdemo.data.model.esri.DBGraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;

import java.util.Map;

/**
 * Created by admin on 2017/9/22.
 */

public class DBCase extends DBGraphicsLayer.DBFeature {
    @Column(ColumnName="FID",ColumnType = Integer.class,AccessMode = ColumnAccessMode.ReadOnly)
    private Integer id;
    @Column(ColumnName="XZQDM",ColumnType = String.class,AccessMode = ColumnAccessMode.ReadOnly)
    private String xzqdm;
    @Column(ColumnName="DCR",ColumnType = String.class,AccessMode = ColumnAccessMode.ReadAndWrite)
    private String dcr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getXzqdm() {
        return xzqdm;
    }

    public void setXzqdm(String xzqdm) {
        this.xzqdm = xzqdm;
    }

    public String getDcr() {
        return dcr;
    }

    public void setDcr(String dcr) {
        this.dcr = dcr;
    }
}
