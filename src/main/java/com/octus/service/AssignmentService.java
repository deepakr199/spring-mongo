package com.octus.service;

import com.octus.dao.AssignmentDao;
import com.octus.exception.StorageException;
import com.octus.model.mongo.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentService.class);

  @Autowired
  private AssignmentDao assignmentDao;

  @Autowired
  private CounterService counterService;

  public Optional<Storage> get(Long id) {
    try {
      return Optional.ofNullable(
          assignmentDao.findOne(Example.of(new Storage(id))));
    } catch (StorageException e) {
      LOGGER.error("Exception while getting Storage for Id: {}", id, e);
      throw new StorageException(e.getMessage(), e);
    }
  }

  public List<Storage> getAll() {
    try {
      return assignmentDao.findAll();
    } catch (Exception e) {
      LOGGER.error("Exception while getting Storages", e);
      throw new StorageException(e.getMessage(), e);
    }
  }

  public Storage save(Storage storage) {
    try {
      Long id = counterService.getIncrementalId("storage");
      storage.setId(id);
      Storage persistedStorage = assignmentDao.save(storage); //Ideally we should get acknowledgement from dao layer
      counterService.incrementSeq("storage", persistedStorage.getId() + 1);
      return persistedStorage;
    } catch (Exception e) {
      LOGGER.error("Exception while saving Storage: {}", storage.toString(), e);
      throw new StorageException(e.getMessage(), e);
    }
  }

  public boolean update(Storage storage) {
    try {
      Optional<Storage> persistedStorage = get(storage.getId());
      if (persistedStorage.isPresent()) {
        storage.setObjectId(persistedStorage.get().getObjectId());
        assignmentDao.save(storage); //Ideally we should get acknowledgement from dao layer
        return true;
      }
      return false;
    } catch (Exception e) {
      LOGGER.error("Exception while updating Storage: {}", storage.toString(), e);
      throw new StorageException(e.getMessage(), e);
    }
  }

  public boolean delete(Long id) {
    try {
      Optional<Storage> storage = get(id);
      if (storage.isPresent()) {
        assignmentDao.delete(storage.get()); //Ideally we should get acknowledgement from dao layer
        return true;
      }
      return false;
    } catch (Exception e) {
      LOGGER.error("Exception while deleting Storage with Id: {}", id, e);
      throw new StorageException(e.getMessage(), e);
    }
  }

}
