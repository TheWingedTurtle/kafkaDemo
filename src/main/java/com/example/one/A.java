package com.example.one;

import java.util.LinkedHashSet;
import java.util.Set;

public class A {
    private int name = 1;

    public static void yellow( int i){
        System.out.println("hello");
    }


    protected void dummy(){

        Set<Integer> s = new LinkedHashSet<>();
        s.iterator().next();

    }
}
