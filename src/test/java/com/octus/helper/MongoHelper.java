package com.octus.helper;

import com.octus.dao.AssignmentDao;
import com.octus.dao.CounterDao;
import com.octus.model.mongo.Storage;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongosExecutable;
import de.flapdoodle.embed.mongo.MongosStarter;
import de.flapdoodle.embed.mongo.runtime.Mongod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MongoHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(MongoHelper.class);

  private boolean isInitialSetup;

  @Autowired
  protected CounterDao counterDao;

  @Autowired
  protected AssignmentDao assignmentDao;

  protected void cleanupDatabase() {
    counterDao.deleteAll();
    assignmentDao.deleteAll();
  }

  protected static void mongoShutdown() throws UnknownHostException {
    Mongod.sendShutdown(InetAddress.getByName("127.0.0.1"), 12345);
  }

  protected void initializeStorageTable(Storage storage) {
    assignmentDao.save(storage);
  }

}
