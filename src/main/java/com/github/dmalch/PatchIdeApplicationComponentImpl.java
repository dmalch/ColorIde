package com.github.dmalch;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;

import static com.intellij.openapi.ui.DialogWrapper.OK_EXIT_CODE;

public class PatchIdeApplicationComponentImpl implements ApplicationComponent, PatchIdeApplicationComponent {

    public static final String SHOW_PATCH_DIALOG = "colorIde.showPatchDialog";
    public static final String USER_ACCEPTED_PATCHING = "colorIde.userAcceptedPatching";

    private PersistenceManager persistenceManager = new PersistenceManagerImpl();

    private ApplicationRestarter restarter = new ApplicationRestarterImpl();

    private PatchIdePatcher patcher = new PatchIdePatcherImpl();

    private PatchingDialogs patchingDialogs = new PatchingDialogsImpl();

    public PatchIdeApplicationComponentImpl() {
        final ImmutableMap.Builder<String, PatchTarget> builder = new ImmutableMap.Builder<String, PatchTarget>();
        builder.put("com/intellij/ui/treeStructure/SimpleNode.class", new PatchTarget("com/intellij/ui/treeStructure", "openapi.jar", "", "120.171"));
        builder.put("com/intellij/ide/util/treeView/PresentableNodeDescriptor.class", new PatchTarget("com/intellij/ide/util/treeView", "openapi.jar", "", "120.171"));
        builder.put("com/intellij/ide/projectView/impl/ProjectViewTree.class", new PatchTarget("com/intellij/ide/projectView/impl", ImmutableList.of("webide.jar", "idea.jar", "rubymine.jar", "pycharm.jar"), "118.308", ""));
        patcher.setFilesToPatch(builder.build());
    }

    @Override
    public void initComponent() {
        if (shouldShowPatchDialog()) {
            doNotShowPatchDialogAnyMore();
            if (userWantsToPatchClasses()) {
                performPatching(false);
            } else {
                performRollback(false);
            }
        }
    }

    @Override
    public void performRollback(final Boolean askBeforeRestart) {
        try {
            userHasRejectedPatching();
            if (patcher.applyRollback()) {
                restarter.restart(askBeforeRestart);
            }
        } catch (RuntimeException e) {
            showErrorToUser(e);
        }
    }

    @Override
    public void performPatching(final Boolean askBeforeRestart) {
        try {
            userHasAcceptedPatching();
            patcher.applyPatch();
            restarter.restart(askBeforeRestart);
        } catch (RuntimeException e) {
            showErrorToUser(e);
        }
    }

    private void showErrorToUser(final RuntimeException e) {
        final Throwable cause = e.getCause();
        if (cause instanceof FileNotFoundException) {
            if (accessDeniedIs(cause)) {
                patchingDialogs.showAccessDeniedError();
            }
        } else {
            throw e;
        }
    }

    private boolean accessDeniedIs(final Throwable cause) {
        return cause.getCause().getMessage().contains("Access is denied");
    }

    private void userHasRejectedPatching() {
        persistenceManager.setBoolean(USER_ACCEPTED_PATCHING, false);
    }

    private void userHasAcceptedPatching() {
        persistenceManager.setBoolean(USER_ACCEPTED_PATCHING, true);
    }

    @Override
    public boolean isUserHasAcceptedPatching() {
        return persistenceManager.getBoolean(USER_ACCEPTED_PATCHING, true);
    }

    private boolean userWantsToPatchClasses() {
        return Objects.equal(OK_EXIT_CODE, patchingDialogs.showPatchDialog());
    }

    private void doNotShowPatchDialogAnyMore() {
        persistenceManager.setBoolean(SHOW_PATCH_DIALOG, false);
    }

    private boolean shouldShowPatchDialog() {
        return isUserHasAcceptedPatching() && (checkShowPatchDialogProperty() || filesAreNotPatched());
    }

    @Override
    public boolean filesAreNotPatched() {
        return !patcher.checkFilesArePatched();
    }

    private boolean checkShowPatchDialogProperty() {
        return persistenceManager.getBoolean(SHOW_PATCH_DIALOG, true);
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "PatchIdeApplicationComponent";
    }

    public PatchingDialogs getPatchingDialogs() {
        return patchingDialogs;
    }

    public void setPatchingDialogs(final PatchingDialogs patchingDialogs) {
        this.patchingDialogs = patchingDialogs;
    }

    public ApplicationRestarter getRestarter() {
        return restarter;
    }

    public void setRestarter(final ApplicationRestarter restarter) {
        this.restarter = restarter;
    }

    public PatchIdePatcher getPatcher() {
        return patcher;
    }

    public void setPatcher(final PatchIdePatcher patcher) {
        this.patcher = patcher;
    }

    public PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public void setPersistenceManager(final PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
}
