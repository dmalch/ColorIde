package com.github.dmalch;

import com.intellij.openapi.ui.Messages;

public class PatchingDialogsImpl implements PatchingDialogs {

    @Override
    public int showPatchDialog() {
        return Messages.showYesNoDialog("Patch IDE plugin wants to patch the instance of your IDE. You can revert or apply patch at settings/Patch IDE menu.", "Confirm Patch",
                "Patch", "Cancel",
                Messages.getQuestionIcon());
    }

    @Override
    public void showAccessDeniedError() {
        Messages.showErrorDialog("Access to the files of your IDE is denied. Please try to launch application as Administrator.", "Access Is Denied");
    }
}
