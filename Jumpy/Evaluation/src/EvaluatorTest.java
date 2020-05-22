public class EvaluatorTest {

    public static void main(String[] args) {
        String testFile = "./TestInput/testshort.jmpy";
        Evaluator evaluator = null;
        try {
            Recognizer recognizer = new Recognizer(testFile);
            Lexeme parseTree = recognizer.run();
            evaluator = new Evaluator();
            evaluator.run(parseTree);
        } catch (Exception e) {
            if (evaluator != null) {
                System.out.println("Runtime error - command " + evaluator.getCommandNum() + ": " + e);
            } else {
                System.out.println(e);
            }
        }
    }

}
