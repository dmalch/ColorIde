package com.github.dmalch;

import com.google.common.io.ByteStreams;
import com.google.common.io.Closeables;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileInputStream;
import de.schlichtherle.truezip.file.TFileOutputStream;
import de.schlichtherle.truezip.file.TVFS;
import de.schlichtherle.truezip.fs.FsSyncException;

import java.io.File;
import java.io.IOException;

import static java.text.MessageFormat.format;

public class JarUtils {

    public static void putIntoJar(final TFile patchFile, final TFile jarEntry) {
        removeObsoleteFile(jarEntry);
        createNewFile(patchFile, jarEntry);
    }

    public static void extractFromJar(final File fileNew, final TFile jarEntry) {
        TFileOutputStream outputStream = null;
        TFileInputStream inputStream = null;
        try {
            outputStream = new TFileOutputStream(fileNew);
            inputStream = new TFileInputStream(jarEntry);

            ByteStreams.copy(inputStream, outputStream);
        } catch (Exception ignored) {
        } finally {
            Closeables.closeQuietly(outputStream);
            Closeables.closeQuietly(inputStream);
        }
        unmount();
    }

    public static TFile jarFile(final File jarFile, final String dirInJar, final String targetFileName) {
        return new TFile(format("{0}/{1}/{2}", jarFile.getAbsolutePath(), dirInJar, targetFileName));
    }

    private static void createNewFile(final TFile patchFilePath, final TFile jar) {
        TFileInputStream inputStream = null;
        TFileOutputStream outputStream = null;
        try {
            inputStream = new TFileInputStream(patchFilePath);
            outputStream = new TFileOutputStream(jar);

            ByteStreams.copy(inputStream, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Closeables.closeQuietly(inputStream);
            Closeables.closeQuietly(outputStream);
        }
        unmount();
    }

    private static void removeObsoleteFile(final TFile jar) {
        try {
            jar.rm();
            unmount();
        } catch (IOException ignored) {
        }
    }

    private static void unmount() {
        try {
            TVFS.umount();
        } catch (FsSyncException ignored) {
        }
    }
}
