package com.example.two;

import com.example.one.A;

public class Store implements BI<String> {

    @Override
    public void hello(String object) {

    }

    public <k extends A,v extends BI<String> & BI2> void someMethod(k k1, v v1){


    }
}
