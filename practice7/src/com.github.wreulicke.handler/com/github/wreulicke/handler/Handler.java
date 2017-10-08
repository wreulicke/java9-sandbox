package com.github.wreulicke.handler;

import java.lang.reflect.*;
import java.util.stream.Stream;

public interface Handler<T> {
  public void handle(T item);
}