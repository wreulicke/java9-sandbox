package com.github.wreulicke;

import com.github.wreulicke.another.AnotherWorld;
import java.lang.reflect.*;
import java.util.stream.Stream;

public class HelloWorld {

  public static void main(String... args) throws Exception {
    AnotherWorld.printAnother();
    System.out.println(AnotherWorld.publicStaticField);
    System.out.println(new AnotherWorld().publicField);
    System.out.println(AnotherWorld.publicStaticMethod());
    System.out.println(new AnotherWorld().publicMethod());

    Method publicStaticMethod = AnotherWorld.class.getMethod("publicStaticMethod");
    System.out.println(publicStaticMethod.invoke(null));
    
    AnotherWorld world = new AnotherWorld();
    Method publicMethod = AnotherWorld.class.getMethod("publicMethod");
    System.out.println(publicMethod.invoke(world));

    Method privateStaticMethod = AnotherWorld.class.getDeclaredMethod("privateStaticMethod");
    privateStaticMethod.setAccessible(true);
    System.out.println(privateStaticMethod.invoke(null));
    
    Method privateMethod = AnotherWorld.class.getDeclaredMethod("privateMethod");
    privateMethod.setAccessible(true);
    System.out.println(privateMethod.invoke(world));
  }
}