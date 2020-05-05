import java.io.IOException;

public class RecognizerTest {

    private static final String srcFile = "../TestInput/test1.jmpy";

    public static void main(String[] args) {
        try {
            Recognizer r = new Recognizer(srcFile);
            r.run();
        } catch (IOException e) {
            System.out.println("Recognizer error: " + e);
        }
    }
}