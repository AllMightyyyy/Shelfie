package org.example.librarysimulation.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    /**
     * Converts a JavaFX Image to a byte array in PNG format.
     *
     * @param image The Image to convert.
     * @return The byte array representation of the image.
     */
    public static byte[] imageToBytes(Image image) {
        if (image == null || image.isError()) {
            System.out.println("Image is null or failed to load, using placeholder image.");
            try {
                Image placeholderImage = new Image(GoogleBooksAPIUtils.class.getResource("/images/book-cover-placeholder.png").toExternalForm());
                image = placeholderImage;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            if (bufferedImage == null) {
                System.out.println("BufferedImage conversion failed, using placeholder.");
                Image placeholderImage = new Image(GoogleBooksAPIUtils.class.getResource("/images/book-cover-placeholder.png").toExternalForm());
                bufferedImage = SwingFXUtils.fromFXImage(placeholderImage, null);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a byte array to a JavaFX Image.
     *
     * @param bytes The byte array to convert.
     * @return The Image object, or null if conversion fails.
     */
    public static Image bytesToImage(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        InputStream is = new ByteArrayInputStream(bytes);
        return new Image(is);
    }
}
