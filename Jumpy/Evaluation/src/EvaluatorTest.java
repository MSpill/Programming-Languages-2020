public class EvaluatorTest {

    public static void main(String[] args) {
        String testFile = "./TestInput/testshort.jmpy";
        try {
            Recognizer recognizer = new Recognizer(testFile);
            Lexeme parseTree = recognizer.run();
            Evaluator evaluator = new Evaluator();
            parseTree.printTree();
            evaluator.run(parseTree);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
