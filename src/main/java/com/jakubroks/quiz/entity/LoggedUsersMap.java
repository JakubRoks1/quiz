package com.jakubroks.quiz.entity;

import lombok.val;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class LoggedUsersMap {

    private final Map<String, User> keyToUser;
    private final Map<String, String> emailToKey;

    public LoggedUsersMap() {
        this.keyToUser = new HashMap<>();
        this.emailToKey = new HashMap<>();
    }

    public Optional<User> getKeyToUser(String key) {
        return Optional.ofNullable(keyToUser.get(key));
    }

    public void addUser(String key, User user) {
        keyToUser.put(key, user);
        emailToKey.put(user.getEmail(), key);
    }

    public boolean removeUserByKey(String key) {
        val removedUser = keyToUser.remove(key);

        if (removedUser == null) {
            return false;
        }

        emailToKey.remove(removedUser.getEmail());
        return true;
    }
}

