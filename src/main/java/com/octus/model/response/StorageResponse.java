package com.octus.model.response;

public class StorageResponse {

  protected Status status;

  public StorageResponse(){
  }

  public StorageResponse(Status status) {
    this.status = status;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
