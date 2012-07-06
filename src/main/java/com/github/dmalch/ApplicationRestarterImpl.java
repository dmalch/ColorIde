package com.github.dmalch;

import com.google.common.base.Objects;
import com.intellij.openapi.application.ex.ApplicationManagerEx;

import static com.intellij.openapi.ui.Messages.*;

public class ApplicationRestarterImpl implements ApplicationRestarter {
    @Override
    public void restart() {
        ApplicationManagerEx.getApplicationEx().restart();
    }

    @Override
    public void askToRestart() {
        if (Objects.equal(showDialog(), OK)) {
            restart();
        }
    }

    private int showDialog() {
        return showYesNoDialog("Restart now?", "Restart", "Restart", "Cancel", getQuestionIcon());
    }
}
