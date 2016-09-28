package com.rkylin.multigates.utils;

import java.lang.reflect.Field;

/**
 * Created by 嘉玮 on 2016-8-29.
 */
public class PrintBean {

    public static void print(Object obj) {
        try {
            Class<?> aClass = obj.getClass();
            Field[] fields = aClass.getDeclaredFields();

            System.out.println("--"+aClass.getSimpleName());
            for (Field f :fields){
                f.setAccessible(true);
                Object o = f.get(obj);

                System.out.println("-- -- "+f.getName()+":"+o);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
