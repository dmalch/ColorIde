package com.github.dmalch;

import com.google.common.base.Objects;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.ui.ThreeStateCheckBox;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

import static com.intellij.util.ui.ThreeStateCheckBox.State.*;

public class PatchIdeSystemSettings implements Configurable {
    private JPanel myPanel;
    private JLabel headerText;
    private ThreeStateCheckBox shouldPatchIdea;

    private PatchIdeApplicationComponent patchIdeApplicationComponent;

    private ThreeStateCheckBox.State initialState;

    @Nls
    @Override
    public String getDisplayName() {
        return "Patch IDE";
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
        return getPatchIdeApplicationComponent().isUserHasAcceptedPatching();
    }

    @Override
    public boolean isModified() {
        return !Objects.equal(shouldPatchIdea.getState(), initialState);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (patchCheckboxIsSelected()) {
            getPatchIdeApplicationComponent().performPatching(false);
        } else if (Objects.equal(shouldPatchIdea.getState(), NOT_SELECTED)) {
            getPatchIdeApplicationComponent().performRollback(false);
        }
    }

    private boolean patchCheckboxIsSelected() {
        return Objects.equal(shouldPatchIdea.getState(), SELECTED);
    }

    @Override
    public void reset() {
        final boolean userHasAcceptedPatching = isUserHasAcceptedPatching();
        final boolean filesArePatched = !getPatchIdeApplicationComponent().filesAreNotPatched();

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

    public ThreeStateCheckBox getShouldPatchIdea() {
        return shouldPatchIdea;
    }

    public PatchIdeApplicationComponent getPatchIdeApplicationComponent() {
        if (patchIdeApplicationComponent == null) {
            patchIdeApplicationComponent = ApplicationManager.getApplication().getComponent(PatchIdeApplicationComponent.class);
        }
        return patchIdeApplicationComponent;
    }

    public void setPatchIdeApplicationComponent(final PatchIdeApplicationComponent patchIdeApplicationComponent) {
        this.patchIdeApplicationComponent = patchIdeApplicationComponent;
    }
}
