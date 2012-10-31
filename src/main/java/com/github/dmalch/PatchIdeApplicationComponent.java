package com.github.dmalch;

public interface PatchIdeApplicationComponent {
    void performRollback(final Boolean askBeforeRestart);

    void performPatching(final Boolean askBeforeRestart);

    boolean isUserHasAcceptedPatching();

    boolean filesAreNotPatched();
}
