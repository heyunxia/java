package com.heyunxia.zeromq;

import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

/**
 * @author heyunxia (love3400wind@163.com)
 * @version 1.0
 * @since 2015-12-03 下午3:15
 */
public class Balance {

    public static void main(String[] args) throws InterruptedException {

        LoadBalance balance = new LoadBalance();
        balance.start();

        Worker worker = new Worker();
        worker.start();

        Client client = new Client();
        client.start();


    }

    public static class Client {
        public void start() {

            for (int i = 0; i < 10; i++) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final ZMQ.Context context = ZMQ.context(1);
                        ZMQ.Socket client = context.socket(ZMQ.REQ);

                        client.connect("ipc://front");//链接front router, 并发送请求

                        for (int i = 0; i < 10; i++) {
                            String threadName = "C" + i + Thread.currentThread().getName();
                            String req = threadName + "\t我来了\t" + i;
                            System.out.println(threadName + "发送：" + req);
                            client.send(req.getBytes(), 0);  // 发送hello请求

                            byte[] data = client.recv(0); ///获取返回的数据
                            System.out.println(threadName + "接收: " + i + "\t" + new String(data));
                        }

                        client.close();
                        context.term();
                    }
                }).start();
            }

        }

    }

    public static class Worker {
        public void start() {

            final ZMQ.Context context = ZMQ.context(1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        ZMQ.Socket worker = context.socket(ZMQ.REQ);

                        worker.connect("ipc://backend"); //连接backend router，用于获取要处理的请求，并发送回去处理结果

                        worker.send("ready".getBytes(), 0); //发送ready，表示当前可用

                        while (!Thread.currentThread().isInterrupted()) {

                            ZMsg msg = ZMsg.recvMsg(worker);  //获取需要处理的请求，其实这里msg最外面的标志frame是router对分配给client的标志frame

                            ZFrame request = msg.removeLast();//最后一个frame其实保存的就是实际的请求数据，这里将其移除，待会用新的frame代替
                            byte[] data = request.getData();


                            String threadName = "S" + Thread.currentThread().getName();
                            String req = threadName + "\t give it to you:\t" + new String(data);

                            ZFrame frame = new ZFrame(req.getBytes());
                            msg.addLast(frame);//将刚刚创建的frame放到msg的最后，worker将会收到
                            msg.send(worker); //将数据发送回去


                        }
                        worker.close();

                    }
                }).start();


            context.term();
        }

    }

    public static class LoadBalance {
        private final LinkedList<ZFrame> workerList;
        private final LinkedList<ZMsg> requestList;

        private final ZMQ.Context context;
        private final ZMQ.Poller poller;

        public LoadBalance() {
            this.workerList = new LinkedList<>();
            this.requestList = new LinkedList<>();
            this.context = ZMQ.context(1);
            this.poller = new ZMQ.Poller(2);
        }

        public void start() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ZMQ.Socket front = context.socket(ZMQ.ROUTER);//创建一个router，用于接收client发送过来的请求，以及向client发送处理结果

                    ZMQ.Socket backend = context.socket(ZMQ.ROUTER);//创建一个router，用于向后面的worker发送数据，然后接收处理的结果

                    front.bind("ipc://front");//监听，等待client的连接
                    backend.bind("ipc://backend"); //监听，等待worker连接

                    //创建pollItem
                    ZMQ.PollItem frontItem = new ZMQ.PollItem(front, ZMQ.Poller.POLLIN);
                    ZMQ.PollItem backendItem = new ZMQ.PollItem(backend, ZMQ.Poller.POLLIN);

                    poller.register(frontItem);//注册pollItem
                    poller.register(backendItem);

                    while (!Thread.currentThread().isInterrupted()) {
                        poller.poll();

                        if (frontItem.isReadable()) { //表示前面有请求发过来了
                            ZMsg msg = ZMsg.recvMsg(frontItem.getSocket());//获取client发送过来的请求，这里router会在实际请求上面套一个连接的标志frame
                            requestList.addLast(msg);//将其挂到请求队列
                        }
                        if (backendItem.isReadable()) {  //这里表示worker发送数据过来了
                            ZMsg msg = ZMsg.recvMsg(backendItem.getSocket()); //获取msg，这里也会在实际发送的数据前面包装一个连接的标志frame
                            //这里需要注意，这里返回的是最外面的那个frame，另外它还会将后面的接着的空的标志frame都去掉
                            ZFrame wokerId = msg.unwrap();//把外面那层包装取下来，也就是router对连接的标志frame
                            workerList.addLast(wokerId);//将当前的worker的标志frame放到worker队列里面，表示这个worker可以用了
                            ZFrame readyOrAddress = msg.getFirst();//这里获取标志frame后面的数据，如果worker刚刚启动，那么应该是发送过来的ready，

                            if ("ready".equals(new String(readyOrAddress.getData()))) {//表示是worker刚刚启动，发过来的ready
                                msg.destroy();
                            } else {
                                msg.send(front); //表示是worker处理完的返回结果，那么返回给客户端
                            }

                        }
                        while (workerList.size() > 0 && requestList.size() > 0) {
                            ZMsg request = requestList.removeFirst();
                            ZFrame worker = workerList.removeFirst();


                            request.wrap(worker);//在request前面包装一层，把可以用的worker的标志frame包装上，这样router就会发给相应的worker的连接
                            request.send(backend);//将这个包装过的消息发送出去
                        }


                    }

                    front.close();
                    backend.close();
                    context.term();
                }
            }).start();

        }
    }
}
