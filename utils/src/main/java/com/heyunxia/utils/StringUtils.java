package com.heyunxia.utils;

import org.junit.Assert;

import java.lang.reflect.Field;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-30 上午11:37
 */
public class StringUtils {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        test3();
    }

    public static void test3() throws NoSuchFieldException, IllegalAccessException {
        String m = "hello,";
        String u = m.concat("world");
        String v = new String(m.substring(0,2));


        Field f = m.getClass().getDeclaredField("value");
        f.setAccessible(true);
        char[] cs = (char[]) f.get(m);
        cs[0] = 'H';


        System.out.println(m); //Hello,world
        System.out.println(u); //hello,world
        System.out.println(v); //hel


    }

    public static void test2() throws NoSuchFieldException, IllegalAccessException {
        String m = "hello,world";
        String u = m.substring(2,10);
        String v = u.substring(4,7);

        Field f = m.getClass().getDeclaredField("value");
        f.setAccessible(true);
        char[] cs = (char[]) f.get(m);
        cs[6] = 'W';



        System.out.println(m); //hello,World
        System.out.println(u);
        System.out.println(v);


    }


    public static void test1() throws NoSuchFieldException, IllegalAccessException {
        String m = "hello,world";
        String n = "hello,world";
        String u = new String(m);
        String v = new String("hello,world");

        Field f = m.getClass().getDeclaredField("value");
        f.setAccessible(true);
        char[] cs = (char[]) f.get(m);
        cs[0] = 'H';

        String p = "Hello,world";
        Assert.assertEquals(p, m);
        Assert.assertEquals(p, n);
        Assert.assertEquals(p, u);
        Assert.assertEquals(p, v);

    }
}
