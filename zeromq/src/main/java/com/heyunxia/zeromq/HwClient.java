package com.heyunxia.zeromq;

import org.zeromq.ZMQ;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-28 上午10:51
 */
public class HwClient {
    public static void main(String[] args) {

        System.out.println(String.format("Version string: %s, Version int: %d",
                ZMQ.getVersionString(),
                ZMQ.getFullVersion()));


        ZMQ.Context context = ZMQ.context(1);

        System.out.println("Connecting to hello world server...");

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://localhost:5555");

        for(int requestNbr=0; requestNbr!=10; requestNbr++) {
            String request = "Hello" +requestNbr;
            System.out.println("Sending " + request);

            requester.send(request.getBytes(), 0);
            byte [] reply = requester.recv(0);
            System.out.println("Received " + new String(reply) + " " + requestNbr);
        }
        requester.close();
        context.term();
    }
}
