package com.heyunxia.concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-30 上午10:54
 */
public class TestTwo {
    public static void main(String[] args) {
        final BlockingQueue<String> queue = new LinkedBlockingDeque<>(16);
        final Semaphore semaphore = new Semaphore(1);

        for (int i = 0; i < 16; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                        try {
                            semaphore.acquire();
                            String log = queue.take();
                            LogClass.printLog(log);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            semaphore.release();
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
