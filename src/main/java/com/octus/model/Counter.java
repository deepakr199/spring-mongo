package com.octus.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "counters")
public class Counter {

  @Field("_id")
  @Id
  private String collection;
  private Long seq;

  public Counter(){
  }

  public Counter(String collection) {
  }

  public Counter(String collection, Long seq) {
    this.collection = collection;
    this.seq = seq;
  }

  public String getCollection() {
    return collection;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public Long getSeq() {
    return seq;
  }

  public void setSeq(Long seq) {
    this.seq = seq;
  }
}
