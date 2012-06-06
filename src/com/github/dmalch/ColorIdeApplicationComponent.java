package com.github.dmalch;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ColorIdeApplicationComponent implements ApplicationComponent {
    public ColorIdeApplicationComponent() {
    }

    public void initComponent() {
        final EditorColorsScheme globalScheme = EditorColorsManager.getInstance().getGlobalScheme();
        UIManager.put("Tree.textForeground", globalScheme.getDefaultForeground());
        UIManager.put("Viewport.foreground", globalScheme.getDefaultForeground());
        UIManager.put("Tree.textBackground", globalScheme.getDefaultBackground());
        UIManager.put("List.background", globalScheme.getDefaultBackground());
        UIManager.put("Tree.background", globalScheme.getDefaultBackground());
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "Applc";
    }
}
