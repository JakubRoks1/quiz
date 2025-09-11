package com.jakubroks.quiz.security;

public class SaltPasswordHasher implements PasswordHasher {

    private final String salt;

    public SaltPasswordHasher(String salt) {
        this.salt = salt;
    }

    public String hashMethod(String password) {
        return Integer.toString((password + salt).hashCode());
    }

    @Override
    public String hash(String raw) {
        return hashMethod(raw);
    }

    @Override
    public boolean matches(String raw, String hashed) {
        return hashMethod(raw).equals(hashed);
    }
}
