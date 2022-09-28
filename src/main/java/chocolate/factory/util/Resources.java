package chocolate.factory.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Resources {

    public static Path getResourceFile(String path) {
        URL url = Resources.class.getClassLoader().getResource(path);
        try {
            assert url != null;
            return new File(url.toURI()).toPath();
        } catch (URISyntaxException e) {
            throw new Error(e);
        }
    }

    public static Properties getProperties(String path) throws IOException {
        Path propertiesFile = Resources.getResourceFile("db.properties");
        try (InputStream in = Files.newInputStream(propertiesFile)) {
            Properties properties = new Properties();
            properties.load(in);
            return properties;
        }
    }
}
