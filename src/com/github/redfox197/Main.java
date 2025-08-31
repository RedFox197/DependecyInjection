package com.github.redfox197;

import com.github.redfox197.di.DIContainer;

public class Main {
    public static void main(String[] args) {
        DIContainer diContainer = new DIContainer();

        diContainer.register(
                ClassLoader.getSystemClassLoader(),
                Main.class.getPackageName()
        );

        TestInject testInject = diContainer.getInstanceOfBean(TestInject.class);
        testInject.test();
    }
}
