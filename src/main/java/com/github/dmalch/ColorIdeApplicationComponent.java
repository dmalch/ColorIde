package com.github.dmalch;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import org.jetbrains.annotations.NotNull;

public class ColorIdeApplicationComponent implements ApplicationComponent {
    private ColorSchemeManager colorSchemeManager = new ColorSchemeManagerImpl();
    private AcceptPatchingDialog acceptPatchingDialog = new AcceptPatchingDialog();
    private RebootDialog rebootDialog = new RebootDialog();

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

        acceptPatchingDialog.showDialog();
        rebootDialog.showDialog();
    }

    public AcceptPatchingDialog getAcceptPatchingDialog() {
        return acceptPatchingDialog;
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

    public void setAcceptPatchingDialog(final AcceptPatchingDialog acceptPatchingDialog) {
        this.acceptPatchingDialog = acceptPatchingDialog;
    }

    public RebootDialog getRebootDialog() {
        return rebootDialog;
    }

    public void setRebootDialog(final RebootDialog rebootDialog) {
        this.rebootDialog = rebootDialog;
    }
}
