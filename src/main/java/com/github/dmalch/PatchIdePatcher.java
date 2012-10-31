package com.github.dmalch;

import com.google.common.collect.ImmutableMap;

public interface PatchIdePatcher {
    void applyPatch();

    boolean checkFilesArePatched();

    boolean applyRollback();

    void setFilesToPatch(ImmutableMap<String, PatchTarget> filesToPatch);
}
