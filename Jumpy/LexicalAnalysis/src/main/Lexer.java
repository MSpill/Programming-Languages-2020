import java.io.*;

class Lexer {
    private PushbackReader input;

    Lexer(String inputFilePath) {
        try {
            System.out.println("Lexing " + inputFilePath);

            File inputFile = new File(inputFilePath);
            input = new PushbackReader(new FileReader(inputFile));
        } catch (FileNotFoundException error) {
            System.out.println("ERROR: Could not find file " + inputFilePath + " for lexing. " + error);
        }
    }

    Lexeme lex() throws IOException {
        /*
         * skipWhiteSpace(); char c = (char) input.read();
         * 
         * if (isEndOfInput(c)) { System.out.println("End of input reached."); return
         * new Lexeme(Type.END_OF_INPUT); }
         */
        return null;
    }

}