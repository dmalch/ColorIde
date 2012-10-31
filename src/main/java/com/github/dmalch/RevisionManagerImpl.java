package com.github.dmalch;

import com.intellij.openapi.application.ApplicationInfo;

import static com.google.common.base.Strings.nullToEmpty;
import static com.intellij.openapi.util.text.StringUtil.compareVersionNumbers;

public class RevisionManagerImpl implements RevisionManager {
    @Override
    public Boolean isCurrentVersionGreaterThen(final String minimalVersion) {
        final String currentVersion = getCurrentVersion();
        final String currentVersionNormalized = normalizeCurrentVersion(currentVersion);
        return compareVersionNumbers(currentVersionNormalized, minimalVersion) >= 0;
    }

    @Override
    public Boolean isCurrentVersionLowerThen(final String maximalRevision) {
        final String currentVersion = getCurrentVersion();
        final String currentVersionNormalized = normalizeCurrentVersion(currentVersion);
        return nullToEmpty(maximalRevision).isEmpty() || compareVersionNumbers(currentVersionNormalized, maximalRevision) < 0;
    }

    private String normalizeCurrentVersion(final String currentVersion) {
        final String currentVersionNormalized;
        if (currentVersion.contains("-")) {
            currentVersionNormalized = currentVersion.split("-")[1];
        } else {
            currentVersionNormalized = currentVersion;
        }
        return currentVersionNormalized;
    }

    @Override
    public String getCurrentVersion() {
        return ApplicationInfo.getInstance().getBuild().asString();
    }
}
