package com.github.dmalch;

import com.intellij.util.ReflectionUtil;
import com.intellij.util.lang.UrlClassLoader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class ClassLoaderUtils {
    private ClassLoaderUtils() {
    }

    public static Object clearUrlClassLoaderCache(final UrlClassLoader urlClassLoader) {
        final Object classPath = getClassPath(urlClassLoader);
        final Object classPathCache = getClassPathCache(classPath);
        final Object classPackagesCache = getClassPackagesCache(classPathCache);
        return invokeMethod(classPackagesCache, "clear");
    }

    public static Object removeElementFromCache(final Object urlClassLoader, final String resourcePath) {
        final Object classPath = getClassPath(urlClassLoader);
        final Object classPathCache = getClassPathCache(classPath);
        final Object classPackagesCache = getClassPackagesCache(classPathCache);

        final String packageName = getPackageName(resourcePath);
        final int hash = packageName.hashCode();

        return removeFromMap(classPackagesCache, hash);
    }

    public static Object setElementInCache(final Object urlClassLoader, final String resourcePath, final Object value) {
        final Object classPath = getClassPath(urlClassLoader);
        final Object classPathCache = getClassPathCache(classPath);
        final Object classPackagesCache = getClassPackagesCache(classPathCache);

        final String packageName = getPackageName(resourcePath);
        final int hash = packageName.hashCode();

        return setToMap(classPackagesCache, hash, value);
    }

    private static Object setToMap(final Object classPackagesCache, final int hash, final Object value) {
        try {
            final Method method = classPackagesCache.getClass().getDeclaredMethod("put", int.class, Object.class);
            return method.invoke(classPackagesCache, hash, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getElementFromCache(final Object urlClassLoader, final String resourcePath) {
        final Object classPath = getClassPath(urlClassLoader);
        final Object classPathCache = getClassPathCache(classPath);
        final Object classPackagesCache = getClassPackagesCache(classPathCache);

        final String packageName = getPackageName(resourcePath);
        final int hash = packageName.hashCode();

        return getFromMap(classPackagesCache, hash);
    }

    private static Object getFromMap(final Object classPackagesCache, final int hash) {
        try {
            final Method method = classPackagesCache.getClass().getDeclaredMethod("get", int.class);
            return method.invoke(classPackagesCache, hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object removeFromMap(final Object classPackagesCache, final int hash) {
        try {
            final Method method = classPackagesCache.getClass().getDeclaredMethod("remove", int.class);
            return method.invoke(classPackagesCache, hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getPackageName(final String resourcePath) {
        final int idx = resourcePath.lastIndexOf('/');
        return idx > 0 ? resourcePath.substring(0, idx) : "";
    }

    public static Object getClassLoaderCacheItemsCount(final UrlClassLoader urlClassLoader) {
        final Object classPath = getClassPath(urlClassLoader);
        final Object classPathCache = getClassPathCache(classPath);
        final Object classPackagesCache = getClassPackagesCache(classPathCache);
        return invokeMethod(classPackagesCache, "size");
    }

    public static Object getResource(final Object classPath, final String param1) {
        try {
            final Method method = classPath.getClass().getDeclaredMethod("getResource", param1.getClass(), boolean.class);
            return method.invoke(classPath, param1, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeMethod(final Object object, final String methodName, final Object param1, final Object param2) {
        try {
            final Method method = object.getClass().getDeclaredMethod(methodName, param1.getClass(), param2.getClass());
            return method.invoke(object, param1, param2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeMethod(final Object object, final String methodName) {
        try {
            final Method method = ReflectionUtil.getMethod(object.getClass(), methodName);
            return method.invoke(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getClassPath(final Object classLoader) {
        return getField(classLoader, "myClassPath");
    }

    public static Object getClassPathCache(final Object classPath) {
        return getField(classPath, "myCache");
    }

    public static Object getClassPackagesCache(final Object classPathCache) {
        return getField(classPathCache, "myClassPackagesCache");
    }

    public static Object getField(final Object object, final String fieldName) {
        Field field = null;

        final ArrayList<Field> fields = ReflectionUtil.collectFields(object.getClass());
        for (final Field each : fields) {
            if (fieldName.equals(each.getName())) {
                field = each;
                break;
            }
        }

        if (field == null) {
            return null;
        }

        field.setAccessible(true);

        final Field modifiersField;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            return field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Comparator<Object> loadersComparator() {
        return new Comparator<Object>() {
            @Override
            public int compare(final Object o1, final Object o2) {
                final String myURL1 = ClassLoaderUtils.getField(o1, "myURL").toString();
                final String myURL2 = ClassLoaderUtils.getField(o2, "myURL").toString();
                final boolean fl1 = myURL1.contains("colorIde");
                final boolean fl2 = myURL2.contains("colorIde");
                if (fl1 && fl2) {
                    return 0;
                } else if (fl1) {
                    return -1;
                } else if (fl2) {
                    return 1;
                }

                return 0;
            }
        };
    }

    public static void sortCache(final ClassLoader classLoader, final String path) {
        final Object[] elementFromCache = (Object[]) getElementFromCache(classLoader, path);

        Arrays.sort(elementFromCache, loadersComparator());

        setElementInCache(classLoader, path, elementFromCache);
    }
}