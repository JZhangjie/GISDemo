package com.demo.jzhangjie.gisdemo.data.model.esri;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by admin on 2017/9/23.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String ColumnName();
    Class ColumnType();
    ColumnAccessMode AccessMode() default ColumnAccessMode.ReadAndWrite;
}
