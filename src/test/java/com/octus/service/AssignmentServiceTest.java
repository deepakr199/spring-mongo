package com.octus.service;

import com.google.common.collect.ImmutableMap;
import com.octus.helper.MongoHelper;
import com.octus.model.mongo.Storage;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:test-context.xml"})
public class AssignmentServiceTest extends MongoHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentServiceTest.class);

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Autowired
  private AssignmentService assignmentService;

  @AfterClass
  public static void tearUp() throws UnknownHostException {
    mongoShutdown();
  }

  @Before
  public void setup() throws Exception {
    LOGGER.info("Setup Storage Service Test");
    cleanupDatabase();
  }

  @Test
  public void getStorage_NonExistingId_ReturnEmpty() {
    Optional<Storage> storage = assignmentService.get(2L);

    Assert.assertFalse(storage.isPresent());
  }

  @Test
  public void getStorage_ExistingId_ReturnStorage() {
    Storage storageToSave = new Storage();
    storageToSave.setDocument(ImmutableMap.of("a", "b", "c", "d"));
    assignmentService.save(storageToSave);
    Optional<Storage> storageOptional = assignmentService.get(1L);

    Assert.assertTrue(storageOptional.isPresent());
    Assert.assertEquals(1, storageOptional.get().getId().longValue());
    Assert.assertEquals(storageToSave.getDocument(), storageOptional.get().getDocument());
  }

  @Test
  public void getAll_WithNoData_ReturnEmpty() {
    List<Storage> storages = assignmentService.getAll();

    Assert.assertTrue(storages.isEmpty());
  }

  @Test
  public void getAll_WithData_ReturnStorages() {
    Storage storageToSave1 = new Storage();
    storageToSave1.setDocument(ImmutableMap.of("a", "b", "c", "d"));
    assignmentService.save(storageToSave1);

    Storage storageToSave2 = new Storage();
    storageToSave2.setDocument(ImmutableMap.of("1", "2", "3", "4"));
    assignmentService.save(storageToSave2);

    List<Storage> storages = assignmentService.getAll();
    Assert.assertEquals(2, storages.size());
    Assert.assertEquals(storageToSave1.getDocument(), storages.get(0).getDocument());
    Assert.assertEquals(storageToSave2.getDocument(), storages.get(1).getDocument());
  }

  @Test
  public void saveStorage_Success_ReturnStorage() {
    Storage storage = new Storage();
    storage.setDocument(ImmutableMap.of("a", "b", "c", "d"));
    Storage persistedStorage = assignmentService.save(storage);

    Assert.assertEquals(1, persistedStorage.getId().longValue());
    Assert.assertEquals(storage.getDocument(), persistedStorage.getDocument());
  }

  @Test
  public void saveStorage_Failure_ReturnStorage() {
    expectedException.expect(Exception.class);

    assignmentService.save(null); //To trigger internal error
  }

  @Test
  public void updateStorage_Success_ReturnTrue() {
    Storage storage = new Storage();
    storage.setDocument(ImmutableMap.of("a", "b", "c", "d"));
    Storage persistedStorage = assignmentService.save(storage);

    persistedStorage.setDocument(ImmutableMap.of("1", "2", "3", "4"));
    boolean status = assignmentService.update(persistedStorage);

    Optional<Storage> updatedStorage = assignmentService.get(1L);

    Assert.assertTrue(status);
    Assert.assertEquals(1, updatedStorage.get().getId().longValue());
    Assert.assertEquals(persistedStorage.getDocument(), updatedStorage.get().getDocument());
  }

  @Test
  public void updateStorage_Failure_ReturnFalse() {
    Storage storage = new Storage();
    storage.setDocument(ImmutableMap.of("a", "b", "c", "d"));
    storage.setId(100L);
    boolean status = assignmentService.update(storage);

    Optional<Storage> updatedStorage = assignmentService.get(100L);

    Assert.assertFalse(status);
    Assert.assertFalse(updatedStorage.isPresent());
  }

  @Test
  public void deleteStorage_ExistingId_ReturnTrue() {
    Storage storage = new Storage();
    storage.setDocument(ImmutableMap.of("a", "b", "c", "d"));
    assignmentService.save(storage);

    boolean status = assignmentService.delete(1L);
    Optional<Storage> deletedStorage = assignmentService.get(1L);
    Assert.assertTrue(status);
    Assert.assertFalse(deletedStorage.isPresent());
  }

  @Test
  public void deleteStorage_NonExistingId_ReturnTrue() {

    boolean status = assignmentService.delete(1L);
    Assert.assertFalse(status);
  }

}
