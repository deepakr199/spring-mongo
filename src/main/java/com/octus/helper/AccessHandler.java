package com.octus.helper;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AccessHandler {

  public static Map<String, String> users = new HashMap<>();

  public static Map<String, String> tokenMap = new HashMap<>();

  public void init() {
    users.put("tom","abc");
    users.put("harry","xyz");
    users.put("admin", "admin");
  }

  public String validate(String user, String password) {
    if (user != null && password != null && password.equals(users.get(user))){
      String token = RandomStringUtils.random(10);
      tokenMap.put(token, user);
      return token;
    }
    return null;
  }

}
