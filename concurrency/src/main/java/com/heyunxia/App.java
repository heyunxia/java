package com.heyunxia;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Hello world!
 */
public class App {


    static Unsafe unsafe;
    private static long nameOffSet;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe"); //Internal reference
            f.setAccessible(true);
            try {
                unsafe = (Unsafe) f.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            nameOffSet = unsafe.objectFieldOffset(App.class.getDeclaredField("name"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    private String name;

    public static void main(String[] args) {
        System.out.println("Hello World!");

        App app = new App();
        app.setName("中国人");

        unsafe.compareAndSwapObject(app, nameOffSet, "中国人", "我来了中国");

        System.out.println(app.getName());

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
