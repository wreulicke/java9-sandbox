package com.github.wreulicke;

import java.lang.reflect.*;
import java.util.stream.Stream;

public class HelloWorld {

  public static void hello(String anotherModuleClass) {
    try {
      Class<?> clazz = Class.forName(anotherModuleClass);
      Object o = clazz.getConstructor().newInstance();
      System.out.println(o);
    } catch(Throwable e){
      System.out.println("error occured");
      e.printStackTrace();
    }
  }
}