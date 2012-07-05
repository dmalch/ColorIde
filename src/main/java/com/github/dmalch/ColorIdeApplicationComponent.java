package com.github.dmalch;

import com.google.gwt.thirdparty.guava.common.base.Objects;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.ui.DialogWrapper.OK_EXIT_CODE;

public class ColorIdeApplicationComponent implements ApplicationComponent {

    private ColorSchemeManager colorSchemeManager = new ColorSchemeManagerImpl();

    private PersistenceManager persistenceManager = new PersistenceManagerImpl();

    private AcceptPatchingDialog acceptPatchingDialog = new AcceptPatchingDialog();

    private RebootDialog rebootDialog = new RebootDialog();

    private ColorIdePatcher patcher = new ColorIdePatcher();

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

        if (shouldShowPatchDialog()) {
            if (Objects.equal(OK_EXIT_CODE, acceptPatchingDialog.showDialog())) {
                patcher.applyPatch();
                rebootDialog.showDialog();
            }
        }
    }

    private boolean shouldShowPatchDialog() {
        return persistenceManager.getBoolean("showPatchDialog", true);
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

    public ColorIdePatcher getPatcher() {
        return patcher;
    }

    public void setPatcher(final ColorIdePatcher patcher) {
        this.patcher = patcher;
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public void setPersistenceManager(final PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
}
