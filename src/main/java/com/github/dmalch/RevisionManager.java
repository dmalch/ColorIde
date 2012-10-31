package com.github.dmalch;

public interface RevisionManager {
    Boolean isCurrentVersionGreaterThen(String minimalRevision);

    Boolean isCurrentVersionLowerThen(String maximalRevision);

    String getCurrentVersion();
}
