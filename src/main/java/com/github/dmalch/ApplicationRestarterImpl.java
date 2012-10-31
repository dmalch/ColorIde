package com.github.dmalch;

import com.google.common.base.Objects;
import com.intellij.openapi.application.ApplicationNamesInfo;
import com.intellij.openapi.application.ex.ApplicationEx;
import com.intellij.openapi.application.ex.ApplicationManagerEx;
import com.intellij.openapi.ui.Messages;

import static com.intellij.openapi.ui.Messages.OK;
import static java.text.MessageFormat.format;

public class ApplicationRestarterImpl implements ApplicationRestarter {

    @Override
    public void restart(final Boolean askBeforeRestart) {
        final ApplicationEx app = ApplicationManagerEx.getApplicationEx();
        if (app.isRestartCapable()) {
            if (!askBeforeRestart || (userWantsToRestart())) {
                app.restart();
            }
        } else {
            if (!askBeforeRestart || (userWantsToShutdown())) {
                app.exit(true);
            }
        }
    }

    private boolean userWantsToRestart() {
        return Objects.equal(showRestartDialog(), OK);
    }

    private boolean userWantsToShutdown() {
        return Objects.equal(showShutdownDialog(), OK);
    }

    private int showRestartDialog() {
        String message = format("Restart {0} to activate changes?", ApplicationNamesInfo.getInstance().getProductName());
        return Messages.showYesNoDialog(message, "Restart", "Restart", "&Postpone", Messages.getQuestionIcon());
    }

    private int showShutdownDialog() {
        String message = format("Shut down {0} to activate changes?", ApplicationNamesInfo.getInstance().getProductName());
        return Messages.showYesNoDialog(message, "Restart", "Shut Down", "&Postpone", Messages.getQuestionIcon());
    }
}
