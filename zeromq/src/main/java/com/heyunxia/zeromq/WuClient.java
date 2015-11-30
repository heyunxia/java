package com.heyunxia.zeromq;

import org.zeromq.ZMQ;

import java.util.StringTokenizer;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-11-28 下午2:28
 */
public class WuClient {
    public static void main(final String[] args) {
        final ZMQ.Context context = ZMQ.context(1);
        for (int i = 0; i < 1; i++) {
            final int task = i;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //  Socket to talk to server
                    System.out.println("Collecting updates from weather server");
                    ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
                    subscriber.connect("tcp://localhost:5556");

                    //  Subscribe to zipcode, default is NYC, 10001
                    String filter = (args.length > 0) ? args[0] : "10001 ";
                    subscriber.subscribe(filter.getBytes());

                    //  Process 100 updates
                    int update_nbr;
                    long total_temp = 0;
                    for (update_nbr = 0; update_nbr < 100; update_nbr++) {
                        //  Use trim to remove the tailing '0' character
                        String string = subscriber.recvStr(0).trim();

                        StringTokenizer sscanf = new StringTokenizer(string, " ");
                        int zipcode = Integer.valueOf(sscanf.nextToken());
                        int temperature = Integer.valueOf(sscanf.nextToken());
                        int relhumidity = Integer.valueOf(sscanf.nextToken());

                        //total_temp += temperature;

                        System.out.println(Thread.currentThread().getName() + "\t" + task + "\t" + update_nbr + "\t Average temperature for zipcode '" + zipcode + " \t filter"
                                + filter + "' was " + temperature + "\t" + relhumidity);

                    }


                    subscriber.close();
                    context.term();
                }
            }).start();

        }


    }

}
