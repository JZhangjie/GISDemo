package com.demo.jzhangjie.gisdemo;

/**
 * Created by admin on 2017/9/20.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
