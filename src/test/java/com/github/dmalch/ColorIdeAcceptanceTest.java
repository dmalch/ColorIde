package com.github.dmalch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

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
    public void testWhenUserAcceptsPatchingThenPatchIsAppliedAndRebootDialogIsShown() {
        givenColorIdeIsRunFirstTime();

        whenAcceptPatching();

        thenPatchIsAppliedAndRebootDialogIsShown();
    }

    @Test
    public void testWhenUserDoesNotAcceptPatchingThenPatchIsNotAppliedAndRebootDialogIsNotShown() {
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
    public void testPatchDialogNotShownAfterFirstRunIfPatchedFilesWereChanged() throws Exception {
        givenColorIdeIsRunAfterFirstTime();
        givenPatchedFilesWereChanged();

        whenStartColorIde();

        thenDialogIsShown();
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
        when(persistenceManager.getBoolean("showPatchDialog", true)).thenReturn(false);
        givenFilesWerePatched();
    }

    private void thenPatchIsNotAppliedAndRebootDialogIsNotShown() {
        verify(patcher, never()).applyPatch();
        verify(applicationRestarter, never()).restart();
        verify(persistenceManager).setBoolean("showPatchDialog", false);
    }

    private void whenDiscardPatching() {
        when(acceptPatchingDialog.showDialog()).thenReturn(CANCEL_EXIT_CODE);
        whenStartColorIde();
    }

    private void thenPatchIsAppliedAndRebootDialogIsShown() {
        verify(patcher).applyPatch();
        verify(applicationRestarter).restart();
        verify(persistenceManager).setBoolean("showPatchDialog", false);
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
        when(persistenceManager.getBoolean("showPatchDialog", true)).thenReturn(true);
    }
}
