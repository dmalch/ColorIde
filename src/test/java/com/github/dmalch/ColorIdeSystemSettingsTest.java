package com.github.dmalch;

import com.intellij.openapi.options.ConfigurationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.github.dmalch.ColorIdeApplicationComponent.USER_ACCEPTED_PATCHING;
import static com.intellij.util.ui.ThreeStateCheckBox.State.DONT_CARE;
import static com.intellij.util.ui.ThreeStateCheckBox.State.NOT_SELECTED;
import static com.intellij.util.ui.ThreeStateCheckBox.State.SELECTED;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ColorIdeSystemSettingsTest {

    @InjectMocks
    private ColorIdeSystemSettings colorIdeSystemSettings;

    @Mock
    private PersistenceManager persistenceManager;

    @Mock
    private ColorIdePatcher patcher;

    @Mock
    private ApplicationRestarter restarter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testLoadSettingsWhenUserRejectedPatchAndNoPatchedFilesFound() throws Exception {
        givenUserRejectedPatch();
        givenNoPatchedFilesFound();

        whenResetSettings();

        thenCheckBoxIsNotSelected();
    }

    @Test
    public void testLoadSettingsWhenUserAcceptedPatchAndNoPatchedFilesFound() throws Exception {
        givenUserAcceptedPatch();
        givenNoPatchedFilesFound();

        whenResetSettings();

        thenCheckBoxIsHalfSelected();
    }

    @Test
    public void testLoadSettingsWhenUserRejectedPatchAndPatchedFilesFound() throws Exception {
        givenUserRejectedPatch();
        givenPatchedFilesFound();

        whenResetSettings();

        thenCheckBoxIsHalfSelected();
    }

    @Test
    public void testLoadSettingsWhenUserAcceptedPatchAndPatchedFilesFound() throws Exception {
        givenUserAcceptedPatch();
        givenPatchedFilesFound();

        whenResetSettings();

        thenCheckBoxIsSelected();
    }

    @Test
    public void testWhenUserAppliesPatchingThenPatchingIsPerformed() throws Exception {
        givenUserRejectedPatch();
        givenNoPatchedFilesFound();

        whenUserAppliesPatching();

        thenPatchingIsPerformed();
    }

    private void thenPatchingIsPerformed() {
        verify(patcher).applyPatch();
        verify(persistenceManager).setBoolean(USER_ACCEPTED_PATCHING, true);
        verify(restarter).askToRestart();
    }

    private void whenUserAppliesPatching() throws ConfigurationException {
        colorIdeSystemSettings.apply();
    }

    private void givenPatchedFilesFound() {
        when(patcher.checkFilesArePatched()).thenReturn(true);
    }

    private void givenNoPatchedFilesFound() {
        when(patcher.checkFilesArePatched()).thenReturn(false);
    }

    private void givenUserAcceptedPatch() {
        when(persistenceManager.getBoolean(eq(USER_ACCEPTED_PATCHING), anyBoolean())).thenReturn(true);
    }

    private void givenUserRejectedPatch() {
        when(persistenceManager.getBoolean(eq(USER_ACCEPTED_PATCHING), anyBoolean())).thenReturn(false);
    }

    private void thenCheckBoxIsSelected() {
        assertThat(colorIdeSystemSettings.getShouldPatchIdea().getState(), equalTo(SELECTED));
    }

    private void thenCheckBoxIsHalfSelected() {
        assertThat(colorIdeSystemSettings.getShouldPatchIdea().getState(), equalTo(DONT_CARE));
    }

    private void thenCheckBoxIsNotSelected() {
        assertThat(colorIdeSystemSettings.getShouldPatchIdea().getState(), equalTo(NOT_SELECTED));
    }

    private void whenResetSettings() {
        colorIdeSystemSettings.reset();
    }
}
