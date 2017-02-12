package com.octus.model.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Document(collection = "storage")
public class Storage {

  @Field("_id")
  @Id
  @JsonIgnore
  private ObjectId objectId;
  @Indexed
  private Long id;
  private Map<String, String> document;

  public Storage() {
  }

  public Storage(Map<String, String> document) {
    this.document = document;
  }

  public ObjectId getObjectId() {
    return objectId;
  }

  public void setObjectId(ObjectId objectId) {
    this.objectId = objectId;
  }

  public Storage(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Map<String, String> getDocument() {
    return document;
  }

  public void setDocument(Map<String, String> document) {
    this.document = document;
  }

  public String toString() {
    return "Id: " + getId() + ", " + "Document: " + getDocument();
  }
}
