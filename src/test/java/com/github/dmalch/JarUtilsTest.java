package com.github.dmalch;

import com.google.common.io.Closeables;
import de.schlichtherle.truezip.file.TFile;
import de.schlichtherle.truezip.file.TFileReader;
import de.schlichtherle.truezip.file.TFileWriter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.junit.Test;

import java.io.*;

import static com.github.dmalch.JarUtils.putIntoJar;
import static java.io.File.separatorChar;
import static java.text.MessageFormat.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class JarUtilsTest {

    public static final String ARCHIVE_JAR = "archive.jar";
    public static final String FILE_TXT = "file.txt";
    public static final String DIR = "dir";

    @Test
    public void testReplaceFileInJar() throws Exception {
        final File file = givenFile(FILE_TXT);
        final File jarFile = givenEmptyJarFile(FILE_TXT, ARCHIVE_JAR, DIR);

        whenPutFileIntoJarFile(file, jarFile, DIR);

        thenFileIsInJarFile(file, jarFile, DIR);
    }

    private File givenEmptyJarFile(final String fileTxt, final String archiveZip, final String dir) {
        Writer writer = null;
        try {
            final File entry = new TFile(format("{0}{1}{2}{1}{3}", archiveZip, separatorChar, dir, fileTxt));
            writer = new TFileWriter(entry);
            writer.write("Hello world!\n");
        } catch (FileNotFoundException e) {
            throw new NotImplementedException();
        } catch (IOException e) {
            throw new NotImplementedException();
        } finally {
            Closeables.closeQuietly(writer);
        }

        return new File("archive.zip");
    }

    private void whenPutFileIntoJarFile(final File file, final File jarFile, final String dirInJar) {
        putIntoJar(file, jarFile, dirInJar);
    }

    private void thenFileIsInJarFile(final File expectedFile, final File jarFile, final String dir) {
        final String expectedFileName = expectedFile.getName();
        final File entry = new TFile(format("{0}{1}{2}{1}{3}", jarFile.getAbsolutePath(), separatorChar, dir, expectedFileName));
        final File expected = new File(expectedFileName);

        Reader reader = null;
        Reader expectedReader = null;
        try {
            reader = new TFileReader(entry);
            expectedReader = new FileReader(expected);
            final String s = IOUtils.toString(reader);
            final String s2 = IOUtils.toString(expectedReader);

            assertThat(s, equalTo(s2));
        } catch (FileNotFoundException e) {
            throw new NotImplementedException();
        } catch (IOException e) {
            throw new NotImplementedException();
        } finally {
            Closeables.closeQuietly(reader);
            Closeables.closeQuietly(expectedReader);
        }
    }

    private File givenFile(final String name) {
        return new File(name);
    }
}
