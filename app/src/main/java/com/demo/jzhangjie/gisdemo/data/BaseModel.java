package com.demo.jzhangjie.gisdemo.data;

/**
 * Created by admin on 2017/9/20.
 */

public abstract class BaseModel {

    public FieldChangeListener mFieldChangeListener;

    public interface FieldChangeListener{
        void fieldChange();
        void fieldChange(String fieldname);
    }

}
