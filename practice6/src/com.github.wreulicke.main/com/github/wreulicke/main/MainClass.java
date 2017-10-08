package com.github.wreulicke.main;

import java.lang.reflect.*;
import java.util.stream.Stream;
import com.github.wreulicke.HelloWorld;

public class MainClass {

  public static void main(String... args) throws Exception {
    HelloWorld.hello("com.github.wreulicke.another.AnotherWorld");
    Module module = ModuleLayer.boot().findModule("com.github.wreulicke").get();
    Module anotherModule = ModuleLayer.boot().findModule("com.github.wreulicke.another").get();
    System.out.println("test");
    anotherModule.addOpens("com.github.wreulicke.another", module);
    HelloWorld.hello("com.github.wreulicke.another.AnotherWorld");
  }
}