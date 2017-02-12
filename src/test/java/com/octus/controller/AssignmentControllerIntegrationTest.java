package com.octus.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.octus.constant.Constant;
import com.octus.helper.MongoHelper;
import com.octus.model.mongo.Storage;
import com.octus.model.response.StorageSuccessResponse;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles({"integration"})
@ContextConfiguration(locations = {"classpath:test-context.xml"})
@WebAppConfiguration
public class AssignmentControllerIntegrationTest extends MongoHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(AssignmentControllerIntegrationTest.class);

  private ObjectMapper mapper = new ObjectMapper();

  private MockMvc mockMvc;
  @Autowired
  private WebApplicationContext wac;

  @AfterClass
  public static void tearUp() throws UnknownHostException {
    mongoShutdown();
  }

  @Before
  public void setup() throws Exception {
    LOGGER.info("Setup Integration Test");
    cleanupDatabase();
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  public void given_ValidRequest_SaveStorage() throws Exception {
    Map<String, String> document = new HashMap<>();
    document.put("a", "b");
    Storage storage = new Storage();
    storage.setDocument(document);
    ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
        .post("/storage")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(mapper.writeValueAsString(storage)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isCreated());
    String responseString = actions.andReturn().getResponse().getContentAsString();
    StorageSuccessResponse response = mapper.readValue(responseString, StorageSuccessResponse.class);
    assertEquals(document, response.getStorage().getDocument());
    assertEquals(1, response.getStorage().getId().longValue());
    assertEquals(Constant.SAVED_STORAGE_SUCSESS, response.getStatus().getMessage());
  }

  @Test
  public void given_InvalidRequest_SaveStorage() throws Exception {
    Map<String, String> document = new HashMap<>();
    document.put("a", "b");
    Storage storage = new Storage();
    storage.setDocument(document);
    ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
        .post("/storage")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(mapper.writeValueAsString(storage)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isCreated());
    String responseString = actions.andReturn().getResponse().getContentAsString();
    StorageSuccessResponse response = mapper.readValue(responseString, StorageSuccessResponse.class);
    assertEquals(document, response.getStorage().getDocument());
    assertEquals(1, response.getStorage().getId().longValue());
    assertEquals(Constant.SAVED_STORAGE_SUCSESS, response.getStatus().getMessage());
  }

  @Test
  public void given_ValidRequest_updateStorage() throws Exception {
    Map<String, String> document = new HashMap<>();
    document.put("a", "b");
    Storage storage = new Storage();
    storage.setDocument(document);
    storage.setId(1L);

    initializeStorageTable(storage);

    Map<String, String> updatedDocument = new HashMap<>();
    updatedDocument.put("a", "b");
    Storage updatedStorage = new Storage();
    updatedStorage.setDocument(updatedDocument);
    updatedStorage.setId(1L);

    ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
        .put("/storage")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(mapper.writeValueAsString(updatedStorage)))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk());

    String responseString = actions.andReturn().getResponse().getContentAsString();
    StorageSuccessResponse response = mapper.readValue(responseString, StorageSuccessResponse.class);

    assertEquals(Constant.UPDATED_STORAGE_SUCCESS, response.getStatus().getMessage());
  }

  @Test
  public void given_ValidRequest_getStorage() throws Exception {
    Map<String, String> document = new HashMap<>();
    document.put("a", "b");
    Storage storage = new Storage();
    storage.setDocument(document);
    storage.setId(1L);

    initializeStorageTable(storage);

    Long id = 1L;

    ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
        .get("/storage/" + id)
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk());

    String responseString = actions.andReturn().getResponse().getContentAsString();
    StorageSuccessResponse response = mapper.readValue(responseString, StorageSuccessResponse.class);

    assertEquals(document, response.getStorage().getDocument());
    assertEquals(id, response.getStorage().getId());
    assertEquals(String.format(Constant.GET_STORAGE_FOR_ID_SUCCESS, id), response.getStatus().getMessage());
  }

  @Test
  public void given_ValidRequest_getAllStorage() throws Exception {
    Map<String, String> document1 = new HashMap<>();
    document1.put("a", "b");
    Storage storage1 = new Storage();
    storage1.setDocument(document1);
    storage1.setId(1L);

    Map<String, String> document2 = new HashMap<>();
    document2.put("a", "b");
    Storage storage2 = new Storage();
    storage2.setDocument(document2);
    storage2.setId(2L);

    initializeStorageTable(storage1);
    initializeStorageTable(storage2);

    ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
        .get("/storage")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk());

    String responseString = actions.andReturn().getResponse().getContentAsString();
    StorageSuccessResponse response = mapper.readValue(responseString, StorageSuccessResponse.class);
    assertEquals(document1, response.getStorages().get(0).getDocument());
    assertEquals(1L, response.getStorages().get(0).getId().longValue());
    assertEquals(document2, response.getStorages().get(1).getDocument());
    assertEquals(2L, response.getStorages().get(1).getId().longValue());
    assertEquals(2, response.getCount().intValue());
    assertEquals(Constant.LISTING_ALL_STORAGES_SUCCESS, response.getStatus().getMessage());
  }

  @Test
  public void given_ValidRequest_deleteStorage() throws Exception {
    Map<String, String> document = new HashMap<>();
    document.put("a", "b");
    Storage storage = new Storage();
    storage.setDocument(document);
    storage.setId(1L);
    initializeStorageTable(storage);

    Long id = 1L;

    ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
        .delete("/storage/" + id)
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(status().isOk());

    String responseString = actions.andReturn().getResponse().getContentAsString();
    StorageSuccessResponse response = mapper.readValue(responseString, StorageSuccessResponse.class);

    assertEquals(String.format(Constant.DELETED_STORAGE_SUCCESS, 1L), response.getStatus().getMessage());
  }

}