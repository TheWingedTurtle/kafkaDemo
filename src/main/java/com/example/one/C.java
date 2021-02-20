package com.example.one;

import com.example.two.B;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class C extends A  {
    public int name = 2;

    public static void yellow(int i){
        Map<String, String> mp = new HashMap<>();
        Map<String, String> mp2;
        mp.put("blah", "a");
        mp.put("blah", "b");
        System.out.println(mp.get("blah"));
    }

    public void yellow(double d){

    }
    @Override
     public void dummy(){
        B b = new B();
    }
}
