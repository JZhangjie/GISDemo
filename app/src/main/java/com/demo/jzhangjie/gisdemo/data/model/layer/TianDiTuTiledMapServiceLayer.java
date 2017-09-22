package com.demo.jzhangjie.gisdemo.data.model.layer;

import android.text.TextUtils;
import android.util.Log;

import com.esri.android.map.TiledServiceLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.io.UserCredentials;

import java.util.Map;
import java.util.concurrent.RejectedExecutionException;


public class TianDiTuTiledMapServiceLayer extends TiledServiceLayer {
    private TileInfo tiandituTileInfo;
    private String url;
    private String layerid;
    private String style;
    private String tileMatrixSet;
    private String format;
    private String type;

    public TianDiTuTiledMapServiceLayer(Builder builder) {
        this(null, true, builder);
    }

    public TianDiTuTiledMapServiceLayer(UserCredentials usercredentials, Builder builder) {
        this(usercredentials, true, builder);
    }

    public TianDiTuTiledMapServiceLayer(UserCredentials usercredentials, boolean flag, Builder builder) {
        super("");
        url = builder.getUrl();
        layerid = builder.getLayerid();
        style = builder.getStyle();
        tileMatrixSet = builder.getTileMatrixSet();
        format = builder.getFormat();
        type = builder.getType();
        setCredentials(usercredentials);

        if (flag)
            try {
                getServiceExecutor().submit(new Runnable() {
                    public final void run() {
                        a.initLayer();
                    }
                    final TianDiTuTiledMapServiceLayer a;
                    {
                        a = TianDiTuTiledMapServiceLayer.this;
                    }
                });
            } catch (RejectedExecutionException _ex) {
            }
    }
    @Override
    protected void initLayer() {
        this.buildTileInfo();
        this.setFullExtent(new Envelope(109.57763671875,20.126953125,117.3779296875,25.521240234375));
        this.setDefaultSpatialReference(SpatialReference.create(4610));   //XIAN80
        super.initLayer();
    }
    //未使用
    public void refresh() {
        try {
            getServiceExecutor().submit(new Runnable() {
                public final void run() {
                    if (a.isInitialized())
                        try {
                            a.b();
                            a.clearTiles();
                            return;
                        } catch (Exception exception) {
                            Log.e("ArcGIS", "Re-initialization of the layer failed.", exception);
                        }
                }
                final TianDiTuTiledMapServiceLayer a;
                {
                    a = TianDiTuTiledMapServiceLayer.this;
                }
            });
            return;
        } catch (RejectedExecutionException _ex) {
            return;
        }
    }
    final void b() throws Exception {
    }

    @Override
    protected byte[] getTile(int level, int col, int row) throws Exception {
        byte[] result = null;
        String tianDiMapUrl = this.getTianDiMapUrl(level, col, row);
        Map<String,String> map = null;
        return com.esri.core.internal.io.handler.a.a(tianDiMapUrl, map);
    }
    @Override
    public TileInfo getTileInfo() {
        return this.tiandituTileInfo;
    }
    /**
     * 获取各等级的url
     *
     * @param level
     * @param col
     * @param row
     * @return
     */
    private String getTianDiMapUrl(int level, int col, int row) {
        StringBuilder serviceUrl = new StringBuilder(url);
        if (!TextUtils.isEmpty(serviceUrl)) {
            if (level<7 || level>16){
              return null;
            }
            serviceUrl.append("?SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0")
                    .append("&LAYER=").append(layerid)
                    .append("&STYLE=").append(style)
                    .append("&TILEMATRIXSET=").append(tileMatrixSet)
                    .append("&TILEMATRIX=").append(level)
                    .append("&TILEROW=").append(row)
                    .append("&TILECOL=").append(col)
                    .append("&FORMAT=").append(format);
        }
        return serviceUrl.toString();
    }

    private void buildTileInfo() {
        Point originalPoint = new Point(-179.99962435, 90.000432);
        double[] res = {
                1.40625000000595,
                0.703125,
                0.3515625,
                0.17578125,
                0.087890625,
                0.0439453125,
                0.02197265625,
                0.010986328125,
                0.0054931640625,
                0.00274658203125,
                0.001373291015625,
                0.0006866455078125,
                0.00034332275390625,
                0.000171661376953125,
                0.0000858306884765625,
                0.0000429153442382813,
                0.0000214576721191406,
                0.0000107288360595703,
                0.00000536441802978516,
                0.00000268220901489258,
                0.00000134110450744629
        };
        double[] scale = {
                590995186.12,
                295497593.05875,
                147748796.529375,
                73874398.2646875,
                36937199.1323438,
                18468599.5661719,
                9234299.78308594,
                4617149.89154297,
                2308574.94577148,
                1154287.47288574,
                577143.736442871,
                288571.868221436,
                144285.934110718,
                72142.9670553589,
                36071.4835276794,
                18035.7417638397,
                9017.87088191986,
                4508.93544095993,
                2254.46772047997,
                1127.23386023998,
                563.616930119991
        };
        int levels = 21;
        int dpi = 96;
        int tileWidth = 256;
        int tileHeight = 256;
        this.tiandituTileInfo = new TileInfo(originalPoint, scale, res, levels, dpi, tileWidth, tileHeight);
        this.setTileInfo(this.tiandituTileInfo);
    }

    public static class Builder {
        private String url;
        private String layerid;
        private String style;
        private String tileMatrixSet;
        private String format;
        private String type;

        public Builder() {

        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        public String getLayerid() {
            return layerid;
        }

        public String getStyle() {
            return style;
        }

        public String getTileMatrixSet() {
            return tileMatrixSet;
        }

        public String getFormat() {
            return format;
        }

        /**
         * 添加URL
         *
         * @param url
         * @return
         */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        /**
         * 添加layerid
         *
         * @param layerid
         * @return
         */
        public Builder setLayerId(String layerid) {
            this.layerid = layerid;
            return this;
        }

        /**
         * 添加style
         *
         * @param style
         * @return
         */
        public Builder setStyle(String style) {
            this.style = style;
            return this;
        }

        /**
         * 添加tileMatrixSet
         *
         * @param tileMatrixSet
         * @return
         */
        public Builder setTileMatrixSet(String tileMatrixSet) {
            this.tileMatrixSet = tileMatrixSet;
            return this;
        }

        public Builder setFormat(String format) {
            this.format = format;
            return this;
        }

        public TianDiTuTiledMapServiceLayer builder() {
            return new TianDiTuTiledMapServiceLayer(this);
        }
    }
}
