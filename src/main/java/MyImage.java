import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class MyImage {
    public void encoded(String message, String file) throws IOException {
        byte bytesInput[] = message.getBytes();
        BufferedImage img;
        File f;

        f = new File(file);
        img = ImageIO.read(f);

        // get the image length ang the first pixel
        int messageSize = bytesInput.length;
        int pixel = img.getRGB(0, 0);

        // a - alpha, r - red, g - green, b - blue
        // shift (0, 8, 16, 24) bits to get the (blue, green, red, alpha) part
        int a = (pixel >> 24) & 0xff;
        int r = (pixel >> 16) & 0xff;
        int g = (pixel >> 8) & 0xff;
        int b = pixel & 0xff;

        // write an image size into the first pixel
        a = ((a >> 2) << 2) | ((messageSize >> 6) & 3);
        r = ((r >> 2) << 2) | ((messageSize >> 4) & 3);
        g = ((g >> 2) << 2) | ((messageSize >> 2) & 3);
        b = ((b >> 2) << 2) | (messageSize & 3);
        pixel = (a << 24) | (r << 16) | (g << 8) | b;

        // set an image size into the first pixel
        img.setRGB(0, 0, pixel);

        int width = img.getWidth();
        int height = img.getHeight();

        int countForWroted = 0;
        for (int x = 0; x < width; x++) {
            // go for the last to bits in each x row
            for (int y = height-2; y < height; y++) {

                pixel = img.getRGB(x, y);

                a = (pixel >> 24) & 0xff;
                r = (pixel >> 16) & 0xff;
                g = (pixel >> 8) & 0xff;
                b = pixel & 0xff;

                // write a char from a message into the two last bits of a not ocupied pixel
                a = ((a >> 2) << 2) | ((bytesInput[countForWroted] >> 6) & 3);
                r = ((r >> 2) << 2) | ((bytesInput[countForWroted] >> 4) & 3);
                g = ((g >> 2) << 2) | ((bytesInput[countForWroted] >> 2) & 3);
                b = ((b >> 2) << 2) | (bytesInput[countForWroted] & 3);
                pixel = (a << 24) | (r << 16) | (g << 8) | b;

                img.setRGB(x, y, pixel);
                countForWroted += 1;
            }
            if (countForWroted == messageSize) break;
        }

        // write encoded image to file with png format
        ImageIO.write(img, "png", f);

    }

    void decoded(String file) throws IOException {
        int messageSize;
        BufferedImage img;
        File f;

        f = new File(file);
        img = ImageIO.read(f);

        int pixel = img.getRGB(0, 0);
        int a = ((pixel >> 24) & 0xff) & 3;
        int r = ((pixel >> 16) & 0xff) & 3;
        int g = ((pixel >> 8) & 0xff) & 3;
        int b = (pixel & 0xff) & 3;

        messageSize = (a << 6) | (r << 4) | (g << 2) | b;

        char[] message = new char[messageSize]; // resulting array
        int countForReaded = 0;

        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; ++x) {
            // go only for two last pixels of each x
            for (int y = height-2; y < height; ++y) {

                pixel = img.getRGB(x, y);
                a = ((pixel >> 24) & 0xff) & 3;
                r = ((pixel >> 16) & 0xff) & 3;
                g = ((pixel >> 8) & 0xff) & 3;
                b = (pixel & 0xff) & 3;

                // write an decoded char into the resulting char array
                message[countForReaded] = (char) ((a << 6) | (r << 4) | (g << 2) | b);
                countForReaded += 1;
            }
            if (countForReaded == messageSize) break;
        }
        System.out.println("The decoded message: ");
        System.out.println(message);
    }
}
