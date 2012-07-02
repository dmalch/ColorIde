package com.github.dmalch;

import com.intellij.util.lang.UrlClassLoader;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static com.github.dmalch.ClassLoaderUtils.*;
import static java.lang.ClassLoader.getSystemClassLoader;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ClassLoaderUtilsTest {

    @Test
    public void testClearClassLoaderCache() throws Exception {
        final UrlClassLoader urlClassLoader = givenClassLoaderWithNonEmptyCache();

        whenClearUrlClassLoaderCache(urlClassLoader);

        thenCacheSizeIsZero(urlClassLoader);
    }

    @Test
    public void testRemoveCustomElementFromCache() throws Exception {
        final UrlClassLoader urlClassLoader = givenClassLoaderWithNonEmptyCache();

        whenRemoveElementFromCache(urlClassLoader);

        thenElementIsRemoved(urlClassLoader);
    }

    @Test
    public void testSetCustomElementInCache() throws Exception {
        final UrlClassLoader urlClassLoader = givenClassLoaderWithNonEmptyCache();

        final String expectedString = "TestString";
        whenSetElementInCache(urlClassLoader, expectedString);

        thenElementIsSet(urlClassLoader, expectedString);
    }

    @Test
    public void testSortLoaders() throws Exception {
        final TestLoader colorIde = new TestLoader("colorIde", "name2");
        final Object[] loaders = givenUnsortedLoaders(colorIde);

        whenSort(loaders);

        thenColorIdeLoaderIsFirst(colorIde, loaders[0]);
    }

    @Test
    public void testSortLoadersWhenArrayAlreadySorted() throws Exception {
        final TestLoader colorIde = new TestLoader("colorIde", "name2");
        final Object[] loaders = givenSortedLoaders(colorIde);

        whenSort(loaders);

        thenColorIdeLoaderIsFirst(colorIde, loaders[0]);
    }

    private void thenColorIdeLoaderIsFirst(final TestLoader colorIde, final Object loader) {
        assertThat((TestLoader) loader, equalTo(colorIde));
    }

    private void whenSort(final Object[] loaders) {
        Arrays.sort(loaders, loadersComparator());
    }

    private Object[] givenUnsortedLoaders(final TestLoader colorIde) {
        final TestLoader testLoader = new TestLoader("test1", "name1");

        final Object[] loaders = new Object[]{testLoader, colorIde};
        assertThat((TestLoader) loaders[0], not(equalTo(colorIde)));
        return loaders;
    }

    private Object[] givenSortedLoaders(final TestLoader colorIde) {
        final TestLoader testLoader = new TestLoader("test1", "name1");

        final Object[] loaders = new Object[]{colorIde, testLoader};
        assertThat((TestLoader) loaders[0], equalTo(colorIde));
        return loaders;
    }


    private void thenElementIsSet(final UrlClassLoader urlClassLoader, final String expectedString) throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        final Object element = getElementFromCache(urlClassLoader, "com/intellij/ui/treeStructure/SimpleNode.class");
        assertThat((String) element, equalTo(expectedString));
    }

    private void whenSetElementInCache(final UrlClassLoader urlClassLoader, final String testString) throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        setElementInCache(urlClassLoader, "com/intellij/ui/treeStructure/Sim1pleNode.class", testString);
    }

    private void thenElementIsRemoved(final UrlClassLoader urlClassLoader) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Object element = getElementFromCache(urlClassLoader, "com/intellij/ui/treeStructure/SimpleNode.class");
        assertThat(element, nullValue());
    }

    private void whenRemoveElementFromCache(final UrlClassLoader urlClassLoader) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        removeElementFromCache(urlClassLoader, "com/intellij/ui/treeStructure/Sim1pleNode.class");
    }

    private void thenCacheSizeIsZero(final UrlClassLoader urlClassLoader) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final Object size = getClassLoaderCacheItemsCount(urlClassLoader);
        assertThat((Integer) size, equalTo(0));
    }

    private void whenClearUrlClassLoaderCache(final UrlClassLoader urlClassLoader) throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        clearUrlClassLoaderCache(urlClassLoader);
    }

    private UrlClassLoader givenClassLoaderWithNonEmptyCache() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final UrlClassLoader urlClassLoader = new UrlClassLoader(getSystemClassLoader());
        urlClassLoader.loadClass("com.intellij.ui.treeStructure.SimpleNode");
        final Object size = getClassLoaderCacheItemsCount(urlClassLoader);

        assertThat((Integer) size, not(equalTo(0)));

        return urlClassLoader;
    }
}
