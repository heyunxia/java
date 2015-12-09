package com.heyunxia.zeromq;

import org.zeromq.ZMQ;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-12-03 上午9:23
 */
public class HwServerExt {

    public static void main(String[] args) {
        final ZMQ.Context context = ZMQ.context(1);

        ZMQ.Socket router = context.socket(ZMQ.ROUTER);

        final ZMQ.Socket dealer = context.socket(ZMQ.DEALER);

        final ZMQ.Socket pub = context.socket(ZMQ.PUB);


        router.bind("ipc://router");
        dealer.bind("ipc://dealer");
        pub.bind("ipc://pub");

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ZMQ.Socket response = context.socket(ZMQ.REP);
                    response.connect("ipc://dealer");
                    while (!Thread.currentThread().isInterrupted()) {
                        byte[] request = response.recv(0);
                        System.out.println("Received : " + new String(request));

                        String reply = "S" + Thread.currentThread().getName() + " World \t" + new String(request);
                        response.send(reply.getBytes(), 0);

                    }
                    response.close();
                }
            }).start();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                ZMQ.Socket sub = context.socket(ZMQ.SUB);
                sub.connect("ipc://pub");
                while (!Thread.currentThread().isInterrupted()) {
                    byte[] data = sub.recv(0);
                    System.out.println("sub:\t" + new String(data));
                    sub.send("".getBytes(), 0);
                }
            }
        }).start();


        ZMQ.proxy(router, dealer, pub);
        router.close();
        dealer.close();
        context.term();
    }
}
