package com.github.dmalch;

import java.net.MalformedURLException;
import java.net.URL;

import static java.text.MessageFormat.format;

public class TestLoader {
    private URL myURL;
    private final String myName;

    public TestLoader(final String url, final String name) {
        try {
            this.myURL = new URL(format("http://{0}", url));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.myName = name;
    }
}
