package com.github.dmalch;

import com.intellij.openapi.application.ex.ApplicationManagerEx;

public class ApplicationRestarterImpl implements ApplicationRestarter {
    @Override
    public void restart() {
        ApplicationManagerEx.getApplicationEx().restart();
    }
}
