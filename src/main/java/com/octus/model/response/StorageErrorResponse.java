package com.octus.model.response;

public class StorageErrorResponse<T> extends StorageResponse {

  private T payload;

  public StorageErrorResponse() {
  }

  public StorageErrorResponse(Status status, T payload) {
    this.status = status;
    this.payload = payload;
  }

  public T getPayload() {
    return payload;
  }

  public void setPayload(T payload) {
    this.payload = payload;
  }
}
