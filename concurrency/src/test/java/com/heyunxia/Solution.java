package com.heyunxia;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

public class Solution {

    public static void main(String[] args) throws ParseException {
        Node node = new Node();
        node.data = 5;
        Print(node);
    }

    static void Print(Node head) {
        if (head == null) {
            return;
        }


        do {
            System.out.println(head.data);
        } while ((head = head.next) != null);
    }

    static Node InsertO(Node head, int data) {
        if (head == null) {
            head = new Node();
            head.data = data;
            return head;
        }
        Node tail = head;
        for (; ; tail = tail.next) {
            if (tail.next == null) {
                break;
            }
        }
        Node myTail = new Node();
        myTail.data = data;
        tail.next = myTail;


        return head;
    }


    static Node Insert(Node head, int data) {
        if (head == null) {
            head = new Node();
            head.data = data;
            return head;
        }
        Node newHead = new Node();
        newHead.data = data;
        newHead.next = head;
        return newHead;
    }

    static Node InsertNth(Node head, int data, int position) {
        if (position < 0) throw new IllegalArgumentException("无效的坐标参数");
        if (position == 0) {
            Node nHead = new Node();
            nHead.data = data;
            nHead.next = head;
            return nHead;
        }
        Node current = head.next;
        Node pre = head;
        int counter = 0;
        for (; ; ) {
            counter++;

            if (position == counter) {
                break;
            }
            pre = current;
            if (pre == null) break;
            current = pre.next;
        }

        Node nNode = new Node();
        nNode.data = data;
        nNode.next = current;
        pre.next = nNode;
        return head;
    }

    static Node Delete(Node head, int position) {
        if (head.next == null) return null;

        if (position < 0) throw new IllegalArgumentException("无效的坐标参数");
        if (position == 0) return head.next;


        Node current = head.next;
        Node pre = head;
        int counter = 0;
        for (; ; ) {
            counter++;

            if (position == counter) {
                break;
            }
            pre = current;
            if (pre == null) break;
            current = pre.next;
        }

        pre.next = current.next;
        return head;

    }


    static void ReversePrint(Node head) {
        Stack stack = new Stack();
        if (head == null) throw new IllegalArgumentException("无效参数");
        do {
            stack.push(head.data);
        } while ((head = head.next) != null);

        int size = stack.size();
        int count = 0;
        while (count < size) {
            System.out.println(stack.pop());
            count ++;
        }
    }


// This is a "method-only" submission.
// You only need to complete this method.

    public static void t() throws ParseException {

        /*Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int arr[] = new int[n];
        for(int arr_i=0; arr_i < n; arr_i++){
            arr[arr_i] = in.nextInt();
        }*/
        int n = 6;
        int positive = 3;
        int negative = 1;
        int zero = 1;
        /*for(int i=0; i<n; i++) {
            if (arr[i] >0) {
                positive += 1;
            } else if(arr[i]<0) {
                negative += 1;
            } else {
                zero += 1;
            }
        }*/
        DecimalFormat df = new DecimalFormat("0.000000");
        float psf = (float) positive / 6;
        String ps = df.format(psf);
        String ns = df.format(negative / n);
        String zs = df.format(zero / n);

        System.out.println(ps);
        System.out.println(ns);
        System.out.println(zs);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfAP = new SimpleDateFormat("h:mm:ssa");
        System.out.println(sdfAP.format(calendar.getTime()));

        Date data = sdfAP.parse("07:05:45PM");


        SimpleDateFormat sdfFull = new SimpleDateFormat("HH:mm:ss");
        System.out.println(sdfFull.format(data));
    }

    public static class Node {
        int data;
        Node next;
    }
}
