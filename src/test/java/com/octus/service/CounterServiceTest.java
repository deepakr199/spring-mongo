package com.octus.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.octus.dao.CounterDao;
import com.octus.model.Counter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class CounterServiceTest {

  private String validCounter = "valid";
  private String inValidCounter = "inValid";

  @Mock
  private CounterDao counterDao;

  @Autowired
  @InjectMocks
  private CounterService counterService;

  @Captor
  private ArgumentCaptor<Counter> counterCaptor;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mockCounterDao();
  }

  private void mockCounterDao() {
    when(counterDao.findOne(validCounter)).thenReturn(getCounter());
    when(counterDao.findOne(inValidCounter)).thenReturn(null);
    when(counterDao.save(any(Counter.class))).thenReturn(new Counter());
  }

  private Counter getCounter() {
    Counter counter = new Counter();
    counter.setCollection("valid");
    counter.setSeq(100L);
    return counter;
  }

  @Test
  public void incrementSeqTest() {
    counterService.incrementSeq(validCounter, 5L);

    verify(counterDao, times(1)).save(counterCaptor.capture());
    Counter counter = counterCaptor.getValue();

    Assert.assertEquals(5L, counter.getSeq().longValue());
    Assert.assertEquals(validCounter, counter.getCollection());
  }

  @Test
  public void getIncrementalIdSuccess() {
    Long seq = counterService.getIncrementalId(validCounter);

    verify(counterDao, times(1)).findOne(validCounter);
    Assert.assertEquals(100L, seq.longValue());
  }

  @Test
  public void getIncrementalIdFailure() {
    Long seq = counterService.getIncrementalId(inValidCounter);

    verify(counterDao, times(1)).findOne(inValidCounter);
    Assert.assertEquals(1L, seq.longValue());
  }
}
