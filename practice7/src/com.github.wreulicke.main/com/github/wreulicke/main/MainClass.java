package com.github.wreulicke.main;

import com.github.wreulicke.dispatcher.*;
import com.github.wreulicke.handler.*;

public class MainClass {

  public static void main(String... args) throws Exception {
    new Dispatcher<>("hogehoge").dispatch(System.out::println);
  }
}