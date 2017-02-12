package com.octus.service;

import com.octus.dao.CounterDao;
import com.octus.model.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounterService {

  @Autowired
  protected CounterDao counterDao;

  public Long getIncrementalId(String collection) {
    Counter counter = counterDao.findOne(collection);
    if (counter == null) {
      incrementSeq(collection, 1L);
      return 1L;
    }
    return counter.getSeq();
  }

  public void incrementSeq(String collection, Long seq) {
    Counter counter = new Counter(collection, seq);
    counterDao.save(counter);
  }
}
