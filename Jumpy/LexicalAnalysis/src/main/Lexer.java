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
        skipWhiteSpace();
        char c = (char) input.read();
        System.out.println(c);
        return new Lexeme(Types.END_OF_INPUT);
    }

    private void skipWhiteSpace() {
        char ch = ' ';
        while (isWhiteSpace(ch)) {
            try {
                ch = (char) input.read();
            } catch (IOException e) {
                System.out.println("Error while skipping whitespace: " + e);
            }
        }

        try {
            input.unread(ch);
        } catch (IOException e) {
            System.out.println("Error while skipping whitespace: " + e);
        }
    }

    private boolean isWhiteSpace(char ch) {
        // newlines are part of my language's grammar, so aren't skipped
        if (ch == ' ' || ch == '\t') {
            return true;
        } else {
            return false;
        }
    }
}