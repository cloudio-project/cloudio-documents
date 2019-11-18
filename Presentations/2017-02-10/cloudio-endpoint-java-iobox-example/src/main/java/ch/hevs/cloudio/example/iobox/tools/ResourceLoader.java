package ch.hevs.cloudio.example.iobox.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Offers a consistent interface in order to load resources from either the file system, the classpath (JAR bundle),
 * the OSGI bundle or from an http server.
 * <br><br>
 * The resource has to be given using an URI scheme as for example "file:/etc/my_config.conf". The following URI
 * schemes are supported:
 * <br>
 * <ul>
 *     <li>
 *         <br><b>classpath:</b><br>
 *         The file is located in the classpath (Either in the JAR file or inside the same OSGi Bundle).
 *     </li>
 *     <li>
 *         <br><b>file:</b><br>
 *         The file is located on the local file system. An absolute path to the file has to be given.
 *     </li>
 *     <li>
 *         <br><b>home:</b><br>
 *         The file is located in the home directory of the actual user. The given path is relative to the user's
 *         home directory.
 *     </li>
 *     <li>
 *         <br><b>http:</b><br>
 *         The resource (file) is located on an HTTP server and will be downloaded on demand.
 *     </li>
 * </ul>
 */
public class ResourceLoader {
    /**
     * Locates the requested resource and returns an open {@link InputStream} if the requested resource could be
     * located, otherwise an exception will be thrown. See {@link ResourceLoader} for supported URI schemes.
     *
     * @param location                  Location as URI of the resource/file.
     * @param target                    The object which will use the resource, this is only important when the URI
     *                                  scheme is "classpath" as the class loader of the given class will be used
     *                                  in order to find the resource.
     * @return                          Ready to use input stream of the resource.
     * @throws FileNotFoundException    If the file could not be found.
     * @throws URISyntaxException       The syntax of the given URI is invalid.
     * @throws MalformedURLException    The URL in the case the scheme is http is invalid.
     * @throws IOException              Any other exception that can happen during opening a resource.
     */
    public static InputStream getResource(final String location, final Object target)
        throws URISyntaxException, IOException {
        return getResource(new URI(location), target);
    }

    /**
     * Locates the requested resource and returns an open {@link InputStream} if the requested resource could be
     * located, otherwise an exception will be thrown. See {@link ResourceLoader} for supported URI schemes.
     *
     * @param uri                       Location of the resource/file.
     * @param target                    The object which will use the resource, this is only important when the URI
     *                                  scheme is "classpath" as the class loader of the given class will be used
     *                                  in order to find the resource.
     * @return                          Ready to use input stream of the resource.
     * @throws FileNotFoundException    If the file could not be found.
     * @throws URISyntaxException       The syntax of the given URI is invalid.
     * @throws MalformedURLException    The URL in the case the scheme is http is invalid.
     * @throws IOException              Any other exception that can happen during opening a resource.
     */
    public static InputStream getResource(final URI uri, final Object target) throws URISyntaxException, IOException {
        // If the file is inside the bundle, use the class loader to open the resource.
        if (uri.getScheme().equals("classpath") || uri.getScheme().equals("bundle")) {
            return target.getClass().getClassLoader().getResourceAsStream(uri.getSchemeSpecificPart());

            // If the file is on the local filesystem, try to open the file and return the input stream.
        } else if (uri.getScheme().equals("file")) {
            Path path = Paths.get(uri);
            File file = path.toFile();
            return new FileInputStream(file);

            // The scheme "home" searches on the home directory of the actual user.
        } else if (uri.getScheme().equals("home")) {
            String homeLocation = "file:" + System.getProperty("user.home") + "/" + uri.getPath();
            Path path = Paths.get(new URI(homeLocation));
            File file = path.toFile();
            return new FileInputStream(file);

            // In the case the scheme is either http or https, open a HTTP/HTTPS input stream.
        } else if (uri.getScheme().equals("http") || uri.getScheme().equals("https")) {
            URL url = uri.toURL();
            return url.openStream();
        } else {
            throw new URISyntaxException(uri.toString(), "Scheme " + uri.getScheme() + " not supported.");
        }
    }

    /**
     * Tries to open an input stream for the given file name by trying to open the stream from the given locations one
     * after another. Once the file could be found in one of the given locations, the input stream to that resource is
     * returned. See {@link ResourceLoader} for supported URI schemes.
     *
     * @param filename                  File name of the resource to open.
     * @param target                    The object which will use the resource, this is only important when the URI
     *                                  scheme is "classpath" as the class loader of the given class will be used
     *                                  in order to find the resource.
     * @param locations                 All the locations as URIs to search for.
     * @return                          Ready to use input stream of the resource.
     * @throws FileNotFoundException    If the file could not be found in any of the given locations.
     */
    public static InputStream getResourceFromLocations(String filename, Object target, String... locations)
        throws FileNotFoundException {
        // Try to open the resource from one location after the other.
        for (String path: locations) {
            // If the location path does not end with a slash, add one.
            if (!path.endsWith("/")) {
                path += "/";
            }
            // Try to open the resource and return the resource if the resource exists.
            //noinspection EmptyCatchBlock
            try {
                return getResource(path + filename, target);
            } catch (Exception exception) {}
        }

        // The resource was found in none of the given locations.
        throw new FileNotFoundException(filename);
    }
}
