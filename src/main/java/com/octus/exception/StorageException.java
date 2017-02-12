package com.octus.exception;

public class StorageException extends RuntimeException {

  public StorageException(String message, Throwable throwable) {
    super(message, throwable);
  }

}
