package de.pitkley.jmccs.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is a utility-class containing (more or less) regularly used helper-functions.
 */
public class Utils {

    /**
     * Creates a {@link File} for a resource that is on the classpath.
     * <p>
     * Adapted from: http://stackoverflow.com/a/4691879/758165
     *
     * @param resourceName the name of the resource that is on the classpath
     * @return a (temporary) file
     */
    public static String createFileForResource(String resourceName) {
        /**
         */
        try {
            InputStream in = Utils.class.getClassLoader().getResourceAsStream(resourceName);
            byte[] buffer = new byte[1024];
            int read;
            File temp = File.createTempFile(resourceName + "-", null);
            FileOutputStream fos = new FileOutputStream(temp);

            while ((read = in.read(buffer)) != -1) {
                fos.write(buffer, 0, read);
            }
            fos.close();
            in.close();

            return temp.getAbsolutePath();
        } catch (NullPointerException | IOException ignored) {
            ignored.printStackTrace();
            return resourceName;
        }
    }
}
