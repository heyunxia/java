package com.heyunxia.concurrency;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-24 上午11:36
 */
public class CountDownLatchDemo {
    public static void main(String[] args) {

        int index = 10;
        int maxThread = 10;
        final CountDownLatch startLatch = new CountDownLatch(1);
        final CountDownLatch endLatch = new CountDownLatch(maxThread);
        for (int i = index; i <= index + maxThread; i++) {
            final int task = i;
            new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + " 我准备好了！");
                        startLatch.await();
                        processTask(task);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        endLatch.countDown();
                    }
                }

                public void processTask(int i) throws InterruptedException {
                    double d = new Random().nextInt(i) * 1000;
                    System.out.println(Thread.currentThread().getName() + " 我要休眠" + (long)d);
                    Thread.sleep((long) d);
                }
            }).start();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();
        startLatch.countDown();
        System.out.println("开始");
        try {
            endLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("共用时：" + (endTime - startTime));
    }
}
