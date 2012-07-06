package com.github.dmalch;

import com.intellij.openapi.ui.Messages;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.github.dmalch.ColorIdeApplicationComponent.SHOW_PATCH_DIALOG;
import static com.github.dmalch.ColorIdeApplicationComponent.USER_ACCEPTED_PATCHING;
import static com.intellij.openapi.ui.Messages.OK;
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
    public void testWhenUserRejectsPatchingThenPatchIsRolledBackAndIfNoChangesIdeIsNotRebooted() {
        givenColorIdeIsRunFirstTime();

        whenDiscardPatching();

        thenPatchIsNotAppliedAndRebootIsNotPerformed();
    }

    @Test
    public void testWhenUserRejectsPatchingThenPatchIsRolledBackAndIfThereAreChangesIdeIsRebooted() {
        givenColorIdeIsRunFirstTime();
        givenSeveralFilesArePatched();

        whenDiscardPatching();

        thenPatchIsNotAppliedAndRebootIsPerformed();
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
        givenNotAllFilesWerePatched();

        whenStartColorIde();

        thenDialogIsShown();
    }

    @Test
    public void testPatchDialogIsNotShownAfterFirstRunWhenUserRejectedPatchEvenIfPatchedFilesWereChanged() throws Exception {
        givenColorIdeIsRunAfterFirstTime();
        givenUserRejectedPatching();
        givenNotAllFilesWerePatched();

        whenStartColorIde();

        thenDialogIsNotShown();
    }

    private void givenSeveralFilesArePatched() {
        when(patcher.applyRollback()).thenReturn(true);
    }

    private void givenUserRejectedPatching() {
        when(persistenceManager.getBoolean(eq(USER_ACCEPTED_PATCHING), anyBoolean())).thenReturn(false);
    }

    private void givenUserAcceptedPatching() {
        when(persistenceManager.getBoolean(eq(USER_ACCEPTED_PATCHING), anyBoolean())).thenReturn(true);
    }

    private void givenNotAllFilesWerePatched() {
        when(patcher.checkFilesArePatched()).thenReturn(false);
    }

    private void givenFilesWerePatched() {
        when(patcher.checkFilesArePatched()).thenReturn(true);
    }

    private void thenDialogIsNotShown() {
        verify(acceptPatchingDialog, never()).showDialog();
    }

    private void thenPatchIsNotAppliedAndRebootIsNotPerformed() {
        thenPatchIsNotApplied();
        verify(applicationRestarter, never()).restart();
    }

    private void thenPatchIsNotAppliedAndRebootIsPerformed() {
        thenPatchIsNotApplied();
        verify(applicationRestarter).restart();
    }

    private void thenPatchIsNotApplied() {
        verify(patcher).applyRollback();
        verify(persistenceManager).setBoolean(SHOW_PATCH_DIALOG, false);
        verify(patcher, never()).applyPatch();
        verify(persistenceManager).setBoolean(USER_ACCEPTED_PATCHING, false);
    }

    private void whenDiscardPatching() {
        when(acceptPatchingDialog.showDialog()).thenReturn(Messages.CANCEL);
        whenStartColorIde();
    }

    private void thenPatchIsAppliedAndRebootDialogIsShown() {
        verify(patcher).applyPatch();
        verify(applicationRestarter).restart();
        verify(persistenceManager).setBoolean(SHOW_PATCH_DIALOG, false);
        verify(persistenceManager).setBoolean(USER_ACCEPTED_PATCHING, true);
    }

    private void whenAcceptPatching() {
        when(acceptPatchingDialog.showDialog()).thenReturn(OK);
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
        givenNotAllFilesWerePatched();
        givenUserAcceptedPatching();
    }

    private void givenColorIdeIsRunAfterFirstTime() {
        when(persistenceManager.getBoolean(eq(SHOW_PATCH_DIALOG), anyBoolean())).thenReturn(false);
        givenFilesWerePatched();
        givenUserAcceptedPatching();
    }
}
