package com.octus.helper;

import org.springframework.beans.factory.annotation.Autowired;

public class Initializer {

  @Autowired
  protected AccessHandler accessHandler;

  public void init() {
    accessHandler.init();
  }
}
