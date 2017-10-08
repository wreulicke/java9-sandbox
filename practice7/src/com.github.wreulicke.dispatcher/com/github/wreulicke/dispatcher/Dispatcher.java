package com.github.wreulicke.dispatcher;

import com.github.wreulicke.handler.Handler;

public class Dispatcher<T> {
  final T item;
  public Dispatcher(T item) {
    this.item = item;
  }

  public void dispatch(Handler<T> handler) {
    handler.handle(item);
  }
}