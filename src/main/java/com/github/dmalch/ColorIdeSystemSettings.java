package com.github.dmalch;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;

import javax.swing.*;

public class ColorIdeSystemSettings implements Configurable {
    private JPanel myPanel;
    private JLabel headerText;
    private JCheckBox patchIdea;

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

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
    }
}
