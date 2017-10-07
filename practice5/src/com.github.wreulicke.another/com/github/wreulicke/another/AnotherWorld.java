package com.github.wreulicke.another;

public class AnotherWorld {
  public static String publicStaticField = "publicStaticField";
  private static String privateStaticField = "privateStaticField";
  public String publicField = "publicField";
  private String privateField = "privateField";

  public static void printAnother() {
    System.out.println("Hello Another World!!");
  }

  public static String publicStaticMethod() {
    return "publicStaticMethod";
  }
  
  private static String privateStaticMethod() {
    return "publicStaticMethod";
  }
  
  public String publicMethod() {
    return "publicMethod";
  }

  private String privateMethod() {
    return "privateMethod";
  }

}