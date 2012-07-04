package com.github.dmalch;

import com.intellij.openapi.editor.colors.EditorColorsScheme;

import java.awt.*;

public interface ColorSchemeManager {
    EditorColorsScheme getGlobalColorScheme();

    void setUiProperty(String propertyName, Color color);
}
