package com.github.dmalch;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;

public class ColorIdeAcceptanceTest {

    private ColorIdeApplicationComponent colorIdeApplicationComponent;

    @Test
    public void testUserDialogIsShownAtFirstStart() throws Exception {
        givenColorIdeIsRunFirstTime();

        whenStartColorIde();

        thenDialogIsShown();
    }

    private void thenDialogIsShown() {
        assertThat(colorIdeApplicationComponent.getDialog().isVisible(), is(true));
    }

    private void whenStartColorIde() {
        colorIdeApplicationComponent.initComponent();
    }

    private void givenColorIdeIsRunFirstTime() {
        colorIdeApplicationComponent = new ColorIdeApplicationComponent();
        colorIdeApplicationComponent.setColorSchemeManager(mock(ColorSchemeManager.class, RETURNS_MOCKS));
    }
}
