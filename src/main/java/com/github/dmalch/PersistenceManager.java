package com.github.dmalch;

public interface PersistenceManager {
    boolean getBoolean(String name, boolean defaultValue);

    void setBoolean(final String name, final boolean value);
}
