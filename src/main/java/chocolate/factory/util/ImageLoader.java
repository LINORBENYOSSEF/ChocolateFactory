package chocolate.factory.util;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ImageLoader {

    private final ConcurrentMap<String, Image> cachedImages;

    public ImageLoader() {
        cachedImages = new ConcurrentHashMap<>();
    }

    public Image getImage(String path) {
        if (cachedImages.containsKey(path)) {
            return cachedImages.get(path);
        }

        Path filePath = Resources.getResourceFile(path);
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            Image image = new Image(inputStream);
            cachedImages.put(path, image);

            return image;
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
