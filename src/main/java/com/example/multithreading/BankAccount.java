package com.example.multithreading;


import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BankAccount {
    private static AtomicInteger balance = new AtomicInteger(1000);

    public void withdraw(){

            if (balance.get() >=  1000)
                updateBalance();
            else System.out.println("balance < 1000");

    }

    private void updateBalance() {
        System.out.println("balance updated");
        update();
    }

    private void update() {
        balance.set(balance.get() - 1000);
        System.out.println(balance);
        String str = "12345";
        str.charAt(0);
    }

    public static void main(String[] args) {
        BankAccount b1 = new BankAccount();
        BankAccount b2 = new BankAccount();

        TaskWithdraw t1;
        TaskWithdraw t2;

        Thread thread1 = new Thread(new TaskWithdraw(b1));
        Thread thread2 = new Thread(new TaskWithdraw(b2));
        thread1.start();
        thread2.start();
    }
}

class TaskWithdraw implements Runnable{
    BankAccount bankAccount;
    @Override
    public void run() {
        bankAccount.withdraw();

        Arrays.asList(1,2,3,4,5,6).stream()
                .filter( d -> d.equals(2))
                .collect(Collectors.toList());
    }
    TaskWithdraw(BankAccount bankAccount){
        this.bankAccount = bankAccount;
    }
}
