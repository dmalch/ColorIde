package com.github.dmalch;

import com.intellij.ide.util.PropertiesComponent;

public class PersistenceManagerImpl implements PersistenceManager {
    @Override
    public boolean getBoolean(final String name, final boolean defaultValue) {
        return PropertiesComponent.getInstance().getBoolean(name, true);
    }

    @Override
    public void setBoolean(final String name, final boolean value) {
        PropertiesComponent.getInstance().setValue(name, Boolean.toString(value));
    }
}
