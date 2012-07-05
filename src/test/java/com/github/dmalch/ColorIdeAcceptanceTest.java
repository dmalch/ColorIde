package com.github.dmalch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.intellij.openapi.ui.DialogWrapper.OK_EXIT_CODE;
import static org.mockito.Answers.RETURNS_MOCKS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ColorIdeAcceptanceTest {

    @Mock
    private AcceptPatchingDialog acceptPatchingDialog;

    @Mock
    private RebootDialog rebootDialog;

    @Mock(answer = RETURNS_MOCKS)
    private ColorSchemeManager colorSchemeManager;

    @Mock
    private ColorIdePatcher patcher;

    @InjectMocks
    private final ColorIdeApplicationComponent colorIdeApplicationComponent = new ColorIdeApplicationComponent();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testUserDialogIsShownAtFirstStart() throws Exception {
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

    private void thenPatchIsAppliedAndRebootDialogIsShown() {
        verify(patcher).applyPatch();
        verify(rebootDialog).showDialog();
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
    }
}
