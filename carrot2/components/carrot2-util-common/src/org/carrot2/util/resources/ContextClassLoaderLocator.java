package org.carrot2.util.resources;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.log4j.Logger;

/**
 * Looks up resources in the thread's context class loader. 
 */
public class ContextClassLoaderLocator implements ResourceLocator
{
    private final static Logger logger = Logger.getLogger(ContextClassLoaderLocator.class);

    /**
     * 
     */
    public Resource [] getAll(String resource, Class clazz)
    {
        try {
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            final Enumeration e = cl.getResources(resource);
            final ArrayList result = new ArrayList();
            while (e.hasMoreElements()) {
                final URL resourceURL = (URL) e.nextElement();
                result.add(new URLResource(resourceURL));
            }
            return (Resource []) result.toArray(new Resource[result.size()]);
        } catch (IOException e) {
            logger.warn("Locator failed to find resources.", e);
            return new Resource [0];
        }
    }
}
