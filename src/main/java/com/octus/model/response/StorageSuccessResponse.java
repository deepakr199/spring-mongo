package com.octus.model.response;

import com.octus.model.mongo.Storage;

import java.util.List;

public class StorageSuccessResponse extends StorageResponse {

  private Storage storage;
  private List<Storage> storages;
  private Integer count;

  public StorageSuccessResponse() {
  }

  public StorageSuccessResponse(Status status, Storage storage) {
    this.status = status;
    this.storage = storage;
  }

  public StorageSuccessResponse(Status status, List<Storage> storages) {
    this.status = status;
    this.storages = storages;
    this.count = storages.size();
  }

  public Storage getStorage() {
    return storage;
  }

  public void setStorage(Storage storage) {
    this.storage = storage;
  }

  public List<Storage> getStorages() {
    return storages;
  }

  public void setStorages(List<Storage> storages) {
    this.storages = storages;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
