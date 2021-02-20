package com.example.multithreading;

public class ThreadExmaple {

    public static void main(String[] args) throws InterruptedException {
        Task task = new Task();
        Thread thread = new Thread(task);
        thread.start();
        Thread.sleep(3000);
        System.out.println("Inside Main ....");
    }
}

class Task implements Runnable{

    @Override
    public void run() {
        System.out.println("Inside Task ....");
        go();
    }

    private void go() {
        System.out.println("Inside go.....");
        more();
    }

    private void more() {
        System.out.println("Inside more .....");
    }


}
