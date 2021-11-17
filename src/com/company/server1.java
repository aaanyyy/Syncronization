package com.company;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class server1 {
    final static ReentrantLock thread1Lock = new ReentrantLock();
    final static ReentrantLock thread2Lock = new ReentrantLock();
    private static final List<Integer> integers = new ArrayList<>();
    final static String monitor = "1";
    public static void main(String[] args) throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 50; i += 2) {
                synchronized (monitor) {
                    System.out.println("thread1 " + i);
                    server1.addNumber(i);
                    try {
                        if (i != 48)
                            monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

        Thread thread2 = new Thread(() -> {
            synchronized (monitor) {
                try {
                    monitor.wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 1; i < 50; i += 2) {
                synchronized (monitor) {
                    System.out.println("thread2 " + i);

                    server1.addNumber(i);
                    //monitor.notifyAll();
                    try {
                        if (i != 49)
                            monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        server1.show();
    }

    public static void show() {
        String array = integers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        System.out.println(array);
    }

    public static void addNumber(int i) {

        integers.add(i);
        monitor.notifyAll();
    }

}
