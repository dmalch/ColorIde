package com.github.dmalch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.github.dmalch.ColorIdeApplicationComponent.SHOW_PATCH_DIALOG;
import static com.github.dmalch.ColorIdeApplicationComponent.USER_ACCEPTED_PATCHING;
import static com.intellij.openapi.ui.DialogWrapper.CANCEL_EXIT_CODE;
import static com.intellij.openapi.ui.DialogWrapper.OK_EXIT_CODE;
import static org.mockito.Answers.RETURNS_MOCKS;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ColorIdeAcceptanceTest {

    @Mock
    private AcceptPatchingDialog acceptPatchingDialog;

    @Mock
    private ApplicationRestarter applicationRestarter;

    @Mock(answer = RETURNS_MOCKS)
    private ColorSchemeManager colorSchemeManager;

    @Mock(answer = RETURNS_MOCKS)
    private PersistenceManager persistenceManager;

    @Mock
    private ColorIdePatcher patcher;

    @InjectMocks
    private final ColorIdeApplicationComponent colorIdeApplicationComponent = new ColorIdeApplicationComponent();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testPatchDialogIsShownAtFirstStart() throws Exception {
        givenColorIdeIsRunFirstTime();

        whenStartColorIde();

        thenDialogIsShown();
    }

    @Test
    public void testWhenUserAcceptsPatchingThenPatchIsAppliedAndIdeIsRebooted() {
        givenColorIdeIsRunFirstTime();

        whenAcceptPatching();

        thenPatchIsAppliedAndRebootDialogIsShown();
    }

    @Test
    public void testWhenUserRejectsPatchingThenPatchIsNotAppliedAndIdeIsNotRebooted() {
        givenColorIdeIsRunFirstTime();

        whenDiscardPatching();

        thenPatchIsNotAppliedAndRebootDialogIsNotShown();
    }

    @Test
    public void testPatchDialogIsNotShownAfterFirstRun() throws Exception {
        givenColorIdeIsRunAfterFirstTime();

        whenStartColorIde();

        thenDialogIsNotShown();
    }

    @Test
    public void testPatchDialogIsShownAfterFirstRunWhenPatchedFilesWereChanged() throws Exception {
        givenColorIdeIsRunAfterFirstTime();
        givenPatchedFilesWereChanged();

        whenStartColorIde();

        thenDialogIsShown();
    }

    @Test
    public void testPatchDialogIsNotShownAfterFirstRunWhenUserRejectedPatchEvenIfPatchedFilesWereChanged() throws Exception {
        givenColorIdeIsRunAfterFirstTime();
        givenUserRejectedPatching();
        givenPatchedFilesWereChanged();

        whenStartColorIde();

        thenDialogIsNotShown();
    }

    private void givenUserRejectedPatching() {
        when(persistenceManager.getBoolean(eq(USER_ACCEPTED_PATCHING), anyBoolean())).thenReturn(false);
    }

    private void givenUserAcceptedPatching() {
        when(persistenceManager.getBoolean(eq(USER_ACCEPTED_PATCHING), anyBoolean())).thenReturn(true);
    }

    private void givenPatchedFilesWereChanged() {
        when(patcher.checkFilesArePatched()).thenReturn(false);
    }

    private void givenFilesWerePatched() {
        when(patcher.checkFilesArePatched()).thenReturn(true);
    }

    private void thenDialogIsNotShown() {
        verify(acceptPatchingDialog, never()).showDialog();
    }

    private void givenColorIdeIsRunAfterFirstTime() {
        when(persistenceManager.getBoolean(eq(SHOW_PATCH_DIALOG), anyBoolean())).thenReturn(false);
        givenFilesWerePatched();
        givenUserAcceptedPatching();
    }

    private void thenPatchIsNotAppliedAndRebootDialogIsNotShown() {
        verify(patcher, never()).applyPatch();
        verify(applicationRestarter, never()).restart();
        verify(persistenceManager).setBoolean(SHOW_PATCH_DIALOG, false);
        verify(persistenceManager).setBoolean(USER_ACCEPTED_PATCHING, false);
    }

    private void whenDiscardPatching() {
        when(acceptPatchingDialog.showDialog()).thenReturn(CANCEL_EXIT_CODE);
        whenStartColorIde();
    }

    private void thenPatchIsAppliedAndRebootDialogIsShown() {
        verify(patcher).applyPatch();
        verify(applicationRestarter).restart();
        verify(persistenceManager).setBoolean(SHOW_PATCH_DIALOG, false);
        verify(persistenceManager).setBoolean(USER_ACCEPTED_PATCHING, true);
    }

    private void whenAcceptPatching() {
        when(acceptPatchingDialog.showDialog()).thenReturn(OK_EXIT_CODE);
        whenStartColorIde();
    }

    private void thenDialogIsShown() {
        verify(acceptPatchingDialog).showDialog();
    }

    private void whenStartColorIde() {
        colorIdeApplicationComponent.initComponent();
    }

    private void givenColorIdeIsRunFirstTime() {
        when(persistenceManager.getBoolean(eq(SHOW_PATCH_DIALOG), anyBoolean())).thenReturn(true);
        givenPatchedFilesWereChanged();
        givenUserAcceptedPatching();
    }
}
