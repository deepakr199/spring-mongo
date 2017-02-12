package com.octus.dao;

import com.octus.model.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CounterDao extends MongoRepository<Counter, String>{
}
