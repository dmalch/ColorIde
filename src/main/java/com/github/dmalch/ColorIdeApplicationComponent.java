package com.github.dmalch;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ColorIdeApplicationComponent implements ApplicationComponent {
    private ColorSchemeManager colorSchemeManager = new ColorSchemeManagerImpl();
    private final JDialog jDialog = new JDialog();

    public ColorIdeApplicationComponent() {
    }

    public void initComponent() {
        final EditorColorsScheme globalScheme = colorSchemeManager.getGlobalColorScheme();

        colorSchemeManager.setUiProperty("Viewport.foreground", globalScheme.getDefaultForeground());
        colorSchemeManager.setUiProperty("Viewport.background", globalScheme.getDefaultBackground());

        colorSchemeManager.setUiProperty("Tree.textForeground", globalScheme.getDefaultForeground());
        colorSchemeManager.setUiProperty("Tree.textBackground", globalScheme.getDefaultBackground());

        colorSchemeManager.setUiProperty("Tree.foreground", globalScheme.getDefaultForeground());
        colorSchemeManager.setUiProperty("Tree.background", globalScheme.getDefaultBackground());

        jDialog.setVisible(true);
    }

    public JDialog getDialog() {
        return jDialog;
    }

    public void disposeComponent() {
    }

    @NotNull
    public String getComponentName() {
        return "ColorIdeApplicationComponent";
    }

    public void setColorSchemeManager(final ColorSchemeManager value) {
        colorSchemeManager = value;
    }
}
