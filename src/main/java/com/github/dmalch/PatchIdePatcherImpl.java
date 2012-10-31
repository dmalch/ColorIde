package com.github.dmalch;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.zip.CRC32;

import static com.github.dmalch.JarUtils.*;
import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.collect.Maps.newHashMap;
import static com.intellij.openapi.application.PathManager.getLibPath;
import static java.text.MessageFormat.format;

public class PatchIdePatcherImpl implements PatchIdePatcher {
    private final Map<String, PatchTarget> filesToPatch = newHashMap();
    private RevisionManager revisionManager = new RevisionManagerImpl();

    @Override
    public void applyPatch() {
        for (final String patchFilePath : filesToPatch.keySet()) {

            final PatchTarget patchTarget = filesToPatch.get(patchFilePath);

            if (revisionManager.isCurrentVersionGreaterThen(patchTarget.getMinRevision()) &&
                    revisionManager.isCurrentVersionLowerThen(patchTarget.getMaxRevision())) {
                final TFile patchFile = getPatchFile(patchFilePath);

                final String targetFileName = patchFile.getName();

                for (final String pathToArchive : patchTarget.getPathToArchives()) {
                    final File jarFile = findFileInLibDirectory(pathToArchive);
                    if (jarFile.exists()) {
                        final TFile jarEntry = jarFile(jarFile, patchTarget.getInnerDir(), targetFileName);

                        final File bakFile = bakFile(patchFile, jarFile);
                        if (!bakFile.exists()) {
                            extractFromJar(bakFile, jarEntry);
                        }
                        putIntoJar(patchFile, jarEntry);
                    }
                }
            }
        }
    }

    private TFile getPatchFile(final String patchFilePath) {
        final URI uri = getPatchFileURI(patchFilePath);
        return new TFile(uri);
    }

    private URI getPatchFileURI(final String patchFilePath) {
        URI uri = null;
        try {
            uri = getClass().getClassLoader().getResource(patchFilePath).toURI();
        } catch (Exception ignored) {
        }
        return firstNonNull(uri, new File(patchFilePath).toURI());
    }

    @Override
    public boolean checkFilesArePatched() {
        boolean filesArePatched = true;

        for (final String patchFilePath : filesToPatch.keySet()) {

            final PatchTarget patchTarget = filesToPatch.get(patchFilePath);

            if (revisionManager.isCurrentVersionGreaterThen(patchTarget.getMinRevision()) &&
                    revisionManager.isCurrentVersionLowerThen(patchTarget.getMaxRevision())) {
                final TFile patchFile = getPatchFile(patchFilePath);

                final String targetFileName = patchFile.getName();

                for (final String pathToArchive : patchTarget.getPathToArchives()) {
                    final File jarFile = findFileInLibDirectory(pathToArchive);
                    final TFile jarEntry = jarFile(jarFile, patchTarget.getInnerDir(), targetFileName);

                    if (jarFile.exists() && filesAreDifferent(patchFile, jarEntry)) {
                        filesArePatched = false;
                    }
                }
            }
        }

        return filesArePatched;
    }

    @Override
    public boolean applyRollback() {
        boolean jarWasModified = false;

        for (final String patchFilePath : filesToPatch.keySet()) {

            final PatchTarget patchTarget = filesToPatch.get(patchFilePath);
            final File patchFile = new File(patchFilePath);

            final String targetFileName = patchFile.getName();

            for (final String pathToArchive : patchTarget.getPathToArchives()) {
                final File jarFile = findFileInLibDirectory(pathToArchive);
                final TFile jarEntry = jarFile(jarFile, patchTarget.getInnerDir(), targetFileName);

                final TFile bakFile = bakFile(patchFile, jarFile);

                if (bakFileExists(bakFile) && filesAreDifferent(bakFile, jarEntry)) {
                    putIntoJar(bakFile, jarEntry);
                    jarWasModified = true;
                }
            }
        }

        return jarWasModified;
    }

    public File findFileInLibDirectory(final String relativePath) {
        return new File(format("{0}/{1}", getLibPath(), relativePath));
    }

    private boolean bakFileExists(final File bakFile) {
        return bakFile.exists();
    }

    private boolean filesAreDifferent(final File origFile, final File jarEntry) {
        try {
            final long orig = calcFileChecksum(origFile);
            final long fromJar = calcFileChecksum(jarEntry);
            return orig != fromJar;
        } catch (Exception e) {
            return true;
        }
    }

    protected long calcFileChecksum(final File file) {
        try {
            return ByteStreams.getChecksum(new InputSupplier<TFileInputStream>() {
                @Override
                public TFileInputStream getInput() throws IOException {
                    return new TFileInputStream(file);
                }
            }, new CRC32());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TFile bakFile(final File patchFile, final File jarFile) {
        return new TFile(format("{0}/{1}.bak", jarFile.getParent(), patchFile.getName()));
    }

    @Override
    public void setFilesToPatch(final ImmutableMap<String, PatchTarget> filesToPatch) {
        this.filesToPatch.putAll(filesToPatch);
    }

    public void setRevisionManager(final RevisionManager revisionManager) {
        this.revisionManager = revisionManager;
    }
}
