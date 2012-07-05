package com.github.dmalch;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

public class AcceptPatchingDialog {

    DialogWrapper.DoNotAskOption option = new DialogWrapper.DoNotAskOption() {
        @Override
        public boolean isToBeShown() {
            return true;
        }

        @Override
        public void setToBeShown(final boolean value, final int exitCode) {
        }

        @Override
        public boolean canBeHidden() {
            return false;
        }

        @Override
        public boolean shouldSaveOptionsOnCancel() {
            return false;
        }

        @Override
        public String getDoNotShowMessage() {
            return "Do not ask me again";
        }
    };

    public int showDialog() {
        return Messages.showYesNoDialog("Color IDE plugin wants to patch your instance of Intellij IDEA", "Confirm patch",
                "Patch", "Cancel",
                Messages.getQuestionIcon(), option);
    }
}
