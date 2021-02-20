package com.example;

import com.example.one.A;
import com.example.one.C;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        C a = new C ();
        a.yellow(10);

        Set<Integer> mp = new TreeSet<>((o1, o2) -> {
            return (Integer) o1.compareTo((Integer) o2);
        });

        //int t = a.name;
    }
}
