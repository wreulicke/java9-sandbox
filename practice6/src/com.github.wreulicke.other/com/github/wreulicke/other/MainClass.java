package com.github.wreulicke.other;

import java.lang.reflect.*;
import java.util.stream.Stream;
import com.github.wreulicke.HelloWorld;

public class MainClass {

  public static void main(String... args) throws Exception {
    HelloWorld.hello("com.github.wreulicke.other.MainClass");
    Module module = ModuleLayer.boot().findModule("com.github.wreulicke.other").get();
    Module anotherModule = ModuleLayer.boot().findModule("com.github.wreulicke").get();
    System.out.println("test");
    module.addExports("com.github.wreulicke.other", anotherModule);
    HelloWorld.hello("com.github.wreulicke.other.MainClass");
  }
}