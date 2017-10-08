package com.github.wreulicke;

import lombok.*;

public class HelloWorld {
  private final String string;

  HelloWorld(String string){
    this.string = string;
  }

  public static void main(String... args){
    val string = "Hello World!!";
    new HelloWorld(string).print();
  }
  
  public void print(){
    System.out.println(string);
  }
}