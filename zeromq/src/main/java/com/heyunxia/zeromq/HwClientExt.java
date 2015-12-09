package com.heyunxia.zeromq;

import org.zeromq.ZMQ;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-28 上午10:51
 */
public class HwClientExt {
    public static void main(String[] args) {


        System.out.println("Connecting to hello world server...");

        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final ZMQ.Context context = ZMQ.context(1);
                    ZMQ.Socket requester = context.socket(ZMQ.REQ);
                    requester.connect("ipc://router");

                    //for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
                    String request = " Hello " + "C" + Thread.currentThread().getName();
                    System.out.println("Sending " + request);

                    requester.send(request.getBytes(), 0);
                    byte[] reply = requester.recv(0);
                    System.out.println("Received " + new String(reply));
                    //}
                    requester.close();
                    context.term();
                }
            }).start();

        }

    }
}
