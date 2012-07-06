package com.github.dmalch;

import com.google.common.base.Objects;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.ui.ThreeStateCheckBox;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

import static com.github.dmalch.ColorIdeApplicationComponent.USER_ACCEPTED_PATCHING;
import static com.intellij.util.ui.ThreeStateCheckBox.State.*;

public class ColorIdeSystemSettings implements Configurable {
    private JPanel myPanel;
    private JLabel headerText;
    private ThreeStateCheckBox shouldPatchIdea;

    private final PersistenceManager persistenceManager = new PersistenceManagerImpl();

    private final ColorIdePatcher patcher = new ColorIdePatcherImpl();

    private ThreeStateCheckBox.State initialState;

    @Nls
    @Override
    public String getDisplayName() {
        return "Color IDE";
    }

    @Override
    public String getHelpTopic() {
        return null;
    }

    @Override
    public JComponent createComponent() {
        return myPanel;
    }

    private boolean isUserHasAcceptedPatching() {
        return persistenceManager.getBoolean(USER_ACCEPTED_PATCHING, true);
    }

    private boolean filesArePatched() {
        return patcher.checkFilesArePatched();
    }

    @Override
    public boolean isModified() {
        return Objects.equal(shouldPatchIdea.getState(), initialState);
    }

    @Override
    public void apply() throws ConfigurationException {
    }

    @Override
    public void reset() {
        final boolean userHasAcceptedPatching = isUserHasAcceptedPatching();
        final boolean filesArePatched = filesArePatched();

        if (userHasAcceptedPatching && filesArePatched) {
            initialState = SELECTED;
        } else if (userHasAcceptedPatching || filesArePatched) {
            initialState = DONT_CARE;
        } else {
            initialState = NOT_SELECTED;
        }

        shouldPatchIdea.setState(initialState);
    }

    @Override
    public void disposeUIResources() {
    }
}
