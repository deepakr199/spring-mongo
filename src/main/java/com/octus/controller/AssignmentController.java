package com.octus.controller;

import com.octus.constant.Constant;
import com.octus.exception.StorageException;
import com.octus.model.mongo.Storage;
import com.octus.model.response.Status;
import com.octus.model.response.StorageErrorResponse;
import com.octus.model.response.StorageResponse;
import com.octus.model.response.StorageSuccessResponse;
import com.octus.service.AssignmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/storage", consumes = "application/json", produces = "application/json")
public class AssignmentController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentController.class);

  @Autowired
  private AssignmentService assignmentService;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<StorageResponse> getAllStorages() {
    LOGGER.info("Fetching all storages");
    try {
      List<Storage> storages = assignmentService.getAll();
      LOGGER.info("Fetched all storages. Total storages: {}", storages.size());
      Status status = new Status(Constant.LISTING_ALL_STORAGES_SUCCESS);
      return new ResponseEntity<StorageResponse>(new StorageSuccessResponse(status, storages), HttpStatus.OK);
    } catch (StorageException e) {
      LOGGER.error("Exception while getting Storages", e.getMessage());
      Status status = new Status(Constant.LISTING_ALL_STORAGES_ERROR);
      return new ResponseEntity<StorageResponse>(new StorageResponse(status), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<StorageResponse> getStorage(@PathVariable Long id) {
    LOGGER.info("Fetch storage with id: {}", id);
    try {
      Optional<Storage> storageOptional = assignmentService.get(id);
      if (storageOptional.isPresent()) {
        Storage storage = storageOptional.get();
        LOGGER.info("Fetched storage with id: {}, storage: {}", id, storage.getDocument());
        Status status = new Status(String.format(Constant.GET_STORAGE_FOR_ID_SUCCESS, id));
        return new ResponseEntity<StorageResponse>(new StorageSuccessResponse(status, storage), HttpStatus.OK);
      }
      Status status = new Status(String.format(Constant.GET_STORAGE_FOR_ID_NOT_FOUND, id));
      return new ResponseEntity<StorageResponse>(new StorageResponse(status), HttpStatus.NOT_FOUND);
    } catch (StorageException e) {
      LOGGER.error("Exception while getting Storage with Id: {}", id, e.getMessage());
      Status status = new Status(Constant.GET_STORAGE_FOR_ID_ERROR);
      return new ResponseEntity<StorageResponse>(new StorageResponse(status), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<StorageResponse> saveStorage(@RequestBody Storage storage) {
    LOGGER.info("Save storage: {}", storage.getDocument());
    try {
      Storage persistedStorage = assignmentService.save(storage);
      LOGGER.info("Saved Storage: {} successfully", persistedStorage.toString());
      Status status = new Status(Constant.SAVED_STORAGE_SUCSESS);
      return new ResponseEntity<StorageResponse>(new StorageSuccessResponse(status, persistedStorage), HttpStatus.CREATED);
    } catch (StorageException e) {
      LOGGER.error("Exception while saving Storage: {}", storage.toString(), e.getMessage());
      Status status = new Status(Constant.SAVED_STORAGE_ERROR);
      return new ResponseEntity<StorageResponse>(new StorageErrorResponse(status, storage), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(method = RequestMethod.PUT)
  public ResponseEntity<StorageResponse> updateStorage(@RequestBody Storage storage) {
    LOGGER.info("Updated storage: {}", storage.getDocument());
    try {
      if(assignmentService.update(storage)) {
        LOGGER.info("Updated Storage: {} successfully", storage.toString());
        Status status = new Status(Constant.UPDATED_STORAGE_SUCCESS);
        return new ResponseEntity<StorageResponse>(new StorageResponse(status), HttpStatus.OK);
      }
      Status status = new Status(Constant.UPDATED_STORAGE_BAD_REQUEST);
      return new ResponseEntity<StorageResponse>(new StorageErrorResponse(status, storage), HttpStatus.BAD_REQUEST);
    } catch (StorageException e) {
      LOGGER.error("Exception while updating Storage: {}", storage.toString(), e.getMessage());
      Status status = new Status(Constant.UPDATED_STORAGE_ERROR);
      return new ResponseEntity<StorageResponse>(new StorageErrorResponse(status, storage), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<StorageResponse> deleteStorage(@PathVariable Long id) {
    LOGGER.info("Delete storage with Id: {}", id);
    try {
      if (assignmentService.delete(id)) {
        LOGGER.info("Deleted Storage: with Id: {} successfully", id);
        Status status = new Status(String.format(Constant.DELETED_STORAGE_SUCCESS, id));
        return new ResponseEntity<StorageResponse>(new StorageResponse(status), HttpStatus.OK);
      }
      Status status = new Status(String.format(Constant.DELETED_STORAGE_BAD_REQUEST, id));
      return new ResponseEntity<StorageResponse>(new StorageResponse(status), HttpStatus.BAD_REQUEST);
    } catch (StorageException e) {
      LOGGER.error("Exception while deleting Storage with Id: {}", id, e.getMessage());
      Status status = new Status(String.format(Constant.DELETED_STORAGE_ERROR, id));
      return new ResponseEntity<StorageResponse>(new StorageResponse(status), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
