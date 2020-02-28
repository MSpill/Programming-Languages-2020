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
        char ch = input.read();
        if (ch == -1) { // check for end of file
            return new Lexeme(Types.END_OF_INPUT);
        }

        switch (ch) {
            case '\n': return new Lexeme(Types.NEWLINE);
            case '=': return new Lexeme(Types.EQUALS);
            case '+': return new Lexeme(Types.PLUS);
            case '-': return new Lexeme(Types.MINUS);
            case '*': return new Lexeme(Types.TIMES);
            case '/': return new Lexeme(Types.DIVIDE);
            case '(': return new Lexeme(Types.OPAREN);
            case ')': return new Lexeme(Types.CPAREN);
            default:
                if (Character.isDigit(ch)) {
                    input.unread(ch);
                    return lexNumber();
                } else if (Character.isLetter(ch)) {
                    input.unread(ch);
                    return lexVariableOrKeyword();
                } else {
                    System.out.println("Unknown character encountered while lexing: " + ch);
                    return new Lexeme(Types.UNKNOWN);
                }
        }
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