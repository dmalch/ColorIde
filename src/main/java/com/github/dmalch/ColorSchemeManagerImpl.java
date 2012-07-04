package com.github.dmalch;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;

import javax.swing.*;
import java.awt.*;

public class ColorSchemeManagerImpl implements ColorSchemeManager {

    @Override
    public EditorColorsScheme getGlobalColorScheme() {
        return EditorColorsManager.getInstance().getGlobalScheme();
    }

    @Override
    public void setUiProperty(final String propertyName, final Color color) {
        UIManager.put(propertyName, color);
    }
}