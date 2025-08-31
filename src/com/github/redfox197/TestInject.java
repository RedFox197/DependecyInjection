package com.github.redfox197;

import com.github.redfox197.di.annotation.Autowired;
import com.github.redfox197.di.annotation.Bean;

@Bean
public class TestInject {
    @Autowired
    private Test testBean;

    public void test() {
        testBean.test();
    }
}
