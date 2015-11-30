package com.heyunxia.zeromq;

import org.zeromq.ZMQ;

import java.util.Random;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-28 下午2:20
 */
public class WuServer {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket publisher = context.socket(ZMQ.PUB);

        publisher.bind("tcp://*:5556");
        publisher.bind("ipc://weather");

        Random srandom = new Random(System.currentTimeMillis());
        while (!Thread.currentThread().isInterrupted()) {
            //  Get values that will fool the boss
            int zipcode, temperature, relhumidity;
            zipcode = 10001;// + srandom.nextInt(10000);
            temperature = srandom.nextInt(215) - 80 + 1;
            relhumidity = srandom.nextInt(50) + 10 + 1;

            //  Send message to all subscribers
            String update = String.format("%05d %d %d", zipcode, temperature, relhumidity);
            publisher.send(update, 0);

            System.out.println(update);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        publisher.close();
        context.term();
    }
}
