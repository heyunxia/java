package com.heyunxia.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-30 上午10:44
 */
public class TestOne {
    public static void main(String[] args) {
        final BlockingQueue<String> queue = new LinkedBlockingDeque<>(4);

        for (int i = 0; i < 4; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String log = queue.take();
                            LogClass.printLog(log);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        for (int i = 0; i < 16; i++) {
            //LogClass.printLog(i + "");
            try {
                queue.put(i + "");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}

class LogClass {
    public static void printLog(String str) {
        System.out.println(Thread.currentThread().getName() + ":" + str + "\t" + System.currentTimeMillis());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
