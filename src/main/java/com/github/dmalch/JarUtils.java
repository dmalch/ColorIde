package com.github.dmalch;

import com.google.common.io.Closeables;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileWriter;
import de.schlichtherle.truezip.file.TVFS;
import org.apache.commons.io.IOUtils;

import java.io.*;

import static java.io.File.separatorChar;
import static java.text.MessageFormat.format;

public class JarUtils {

    public static void putIntoJar(final File file, final File jarFile, final String dirInJar) {
        removeObsoleteFile(file, jarFile, dirInJar);
        createNewFile(file, jarFile, dirInJar);
    }

    private static void createNewFile(final File file, final File jarFile, final String dirInJar) {
        Writer writer = null;
        try {
            final TFile jar = new TFile(format("{0}{1}{2}{1}{3}", jarFile.getAbsolutePath(), separatorChar, dirInJar, file.getName()));
            writer = new TFileWriter(jar);
            final FileReader input = new FileReader(file);

            IOUtils.copy(input, writer);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Closeables.closeQuietly(writer);
        }
    }

    private static void removeObsoleteFile(final File file, final File jarFile, final String dirInJar) {
        try {
            final TFile jar = new TFile(format("{0}{1}{2}{1}{3}", jarFile.getAbsolutePath(), separatorChar, dirInJar, file.getName()));
            jar.rm();
            TVFS.umount();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
