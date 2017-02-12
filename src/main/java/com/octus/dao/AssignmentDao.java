package com.octus.dao;

import com.octus.model.mongo.Storage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentDao extends MongoRepository<Storage, Long> {

}
