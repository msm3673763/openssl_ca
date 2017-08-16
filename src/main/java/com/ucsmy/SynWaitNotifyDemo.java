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
public class SynWaitNotifyDemo {

    public static void main(String[] args) {
        final business business = new business();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=1;i<51;i++) {
                    business.sub(i);
                }
            }
        });
        t.start();

        for (int i=1;i<51;i++) {
            business.main(i);
        }
    }
}

class business {
    private boolean flag = true;

    public synchronized void sub(int i) {
        while (!flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j=1;j<11;j++) {
            System.out.println("子线程运行第" + i + "次，输出次数：" + j);
        }
        flag = false;
        this.notify();
    }

    public synchronized void main(int i) {
        while (flag) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j=1;j<101;j++) {
            System.out.println("主线程运行第" + i + "次，输出次数：" + j);
        }
        flag = true;
        this.notify();
    }
}