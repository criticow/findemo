package com.waddahex.financeiroapi.security;

import com.waddahex.financeiroapi.model.User;

public class RequestContext {
  private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

  public static void setUser(User user) {
    userHolder.set(user);
  }

  public static User getUser() {
    return userHolder.get();
  }

  public static void clear() {
    userHolder.remove();
  }
}

