package com.heyunxia.zeromq;

import org.zeromq.ZMQ;

import java.util.Random;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-28 上午10:45
 */
public class HwServer {
    public static void main(String[] args) throws InterruptedException {
        ZMQ.Context context = ZMQ.context(2);
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:5555");

        while (!Thread.currentThread().isInterrupted()) {
            byte[] request = responder.recv(0);
            System.out.println("Received : " + new String(request));

            Thread.sleep(1000);

            int random = new Random().nextInt(10000);
            String reply = "World" + random;
            responder.send(reply.getBytes(), 0);
        }

        responder.close();
        context.term();

    }
}
