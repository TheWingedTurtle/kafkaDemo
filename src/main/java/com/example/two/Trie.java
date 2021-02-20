package com.example.two;

import java.util.HashSet;
import java.util.Set;

public class Trie {

    private static Trie root = new Trie('.');
    char value;
    Set<Trie> children = new HashSet<>(26);
    boolean isWord;

    Trie(char c){
        value = c;
    }

    static boolean search(String target){
        int l = target.length();
        Trie current = root;
        for(int i =0 ;i < l ; i++){
            char c  = target.charAt(i);
        }
        return true;
    }
}
