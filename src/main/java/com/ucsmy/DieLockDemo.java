package com.ucsmy;/*
 * Copyright (c) 2017 UCSMY.
 * All rights reserved.
 * Created on 2017/8/16

 * Contributors:
 *      - initial implementation
 */

/**
 * 暂无描述
 *
 * @author ucs_masiming
 * @since 2017/8/16
 */
public class DieLockDemo {

    public static void main(String[] args) {
        Thread threadA = new Thread(new DieLock(true));
        threadA.start();
        Thread threadB = new Thread(new DieLock(false));
        threadB.start();
    }
}

class DieLock implements Runnable {

    public boolean flag;
    public DieLock(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        if (flag) {
            synchronized (MyLock.objA) {
                System.out.println("if ... objA");
                synchronized (MyLock.objB) {
                    System.out.println("if ... objB");
                }
            }
        } else {
            synchronized (MyLock.objB) {
                System.out.println("if .. objB");
                synchronized (MyLock.objA) {
                    System.out.println("if ... objA");
                }
            }
        }
    }
}

class MyLock {
    public static final Object objA = new Object();
    public static final Object objB = new Object();
}
