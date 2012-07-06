package com.github.dmalch;

import com.intellij.openapi.ui.Messages;

public class AcceptPatchingDialog {

    public int showDialog() {
        return Messages.showYesNoDialog("Color IDE plugin wants to patch your instance of Intellij IDEA", "Confirm patch",
                "Patch", "Cancel",
                Messages.getQuestionIcon());
    }
}
