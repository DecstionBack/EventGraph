package com.ansj.vec.test;

import java.io.IOException;

import com.ansj.vec.Word2VEC;

public class Test {
    public static void main(String[] args) throws IOException {
        Word2VEC w1 = new Word2VEC() ;
        w1.loadJavaModel("vectors.mod") ;
        
        System.out.println(w1.distance("五中全会"));
        
        System.out.println(w1.distance("习近平"));
        
        System.out.println(w1.distance("邓小平"));
        
        
        System.out.println(w1.distance("东方"));
        
        System.out.println(w1.distance("船"));
    	
        
    }
}
