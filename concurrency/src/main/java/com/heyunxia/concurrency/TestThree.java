package com.heyunxia.concurrency;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-30 上午10:58
 */
public class TestThree extends Thread {
    private String key;
    private String value;

    public TestThree(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public TestThree(String key, String keyT, String value) {
        this.key = key + keyT;
        this.value = value;
    }

    public static void main(String[] args) {
        Thread a = new TestThree("1", "", "1");
        Thread b = new TestThree("1", "", "2");
        Thread c = new TestThree("3", "", "3");
        Thread d = new TestThree("4", "", "4");

        a.start();
        b.start();
        c.start();
        d.start();
    }

    @Override
    public void run() {
        PrintLog.print(this.key, this.value);
    }
}

class PrintLog {
    //static List<Object> list = new ArrayList<>();
    static CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<>();

    public static void print(Object key, Object value) {


        Object o = key;
        if (!list.contains(key)) {
            list.add(key);
        } else {
            Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Object preO = iterator.next();
                if (preO.equals(o)) {
                    o = preO;
                    break;
                }
            }
        }


        //synchronized (key.toString().intern()) {
        synchronized (o) {
            System.out.println(key + ":" + value + ":" + System.currentTimeMillis() / 1000);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
