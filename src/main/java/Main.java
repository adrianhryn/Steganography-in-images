import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException{
        String filename = "E:\\другий курс\\OOP\\stenography\\src\\main\\resources\\images\\kit.png"; // copy your
                                                                                             // directory here
        String message = "adrian"; // enter a message for encoding and decoding it
        MyImage img = new MyImage();
        img.encoded(message, filename);
        img.decoded(filename);
    }

}