package com.jakubroks.quiz.entity;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoggedUsersMap {

    private final Map<String, User> loggedUsers;
    private final Map<String, String> emailToKey;

    public LoggedUsersMap() {
        this.loggedUsers = new HashMap<>();
        this.emailToKey = new HashMap<>();
    }

    public Map<String, User> getLoggedUsers() {
        return loggedUsers;
    }

    public void addUser(String key, User user) {
        loggedUsers.put(key, user);
        emailToKey.put(user.getEmail(), key);
    }

    public void removeUserByKey(String key) {
        User user = loggedUsers.remove(key);
        emailToKey.remove(user.getEmail());
    }

}

