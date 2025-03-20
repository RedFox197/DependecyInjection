package com.github.redfox197;

import com.github.redfox197.di.annotation.Autowired;
import com.github.redfox197.di.annotation.Bean;

@Bean
public class Albero {
    @Autowired
    private Fiore fiore;

    public void test() {
        System.out.println("Albero");
        fiore.test();
    }
}
