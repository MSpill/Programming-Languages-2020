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
        try {
            skipWhiteSpace();
            int charAsInt = input.read();
            if (charAsInt == 65535) { // check for end of file
                return new Lexeme(Types.END_OF_INPUT);
            }
            char ch = (char) charAsInt;

            switch (ch) {
                case '\n':
                    return new Lexeme(Types.NEWLINE);
                case '=':
                    return new Lexeme(Types.EQUALS);
                case '+':
                    return new Lexeme(Types.PLUS);
                case '-':
                    return new Lexeme(Types.MINUS);
                case '*':
                    return new Lexeme(Types.TIMES);
                case '/':
                    return new Lexeme(Types.DIVIDE);
                case '(':
                    return new Lexeme(Types.OPAREN);
                case ')':
                    return new Lexeme(Types.CPAREN);
                default:
                    if (Character.isDigit(ch)) {
                        input.unread(ch);
                        return lexNumber();
                    } else if (Character.isLetter(ch)) {
                        input.unread(ch);
                        return lexVariableOrKeyword();
                    } else {
                        throw new IOException("Encountered unknown character while lexing: " + ch);
                    }
            }
        } catch (IOException e) {
            System.out.println("Error while lexing: " + e);
            return new Lexeme(Types.UNKNOWN);
        }
    }

    private Lexeme lexNumber() {
        try {
            String token = "";
            char ch = (char) input.read();
            while (Character.isDigit(ch)) {
                token = token + ch;
                ch = (char) input.read();
            }

            input.unread(ch);
            return new Lexeme(Types.NUMBER, Integer.parseInt(token));
        } catch (IOException e) {
            System.out.println("Error while lexing number: " + e);
            return new Lexeme(Types.UNKNOWN);
        }
    }

    private Lexeme lexVariableOrKeyword() {
        try {
            String token = "";
            char ch = (char) input.read();
            while (Character.isDigit(ch) || Character.isLetter(ch)) {
                token = token + ch;
                ch = (char) input.read();
            }

            input.unread(ch);

            if (token.equals("jmp")) {
                return new Lexeme(Types.JMP);
            } else if (token.equals("jmpabs")) {
                return new Lexeme(Types.JMPABS);
            } else if (token.equals("println")) {
                return new Lexeme(Types.PRINTLN);
            } else if (token.equals("int")) {
                return new Lexeme(Types.INT);
            } else {
                return new Lexeme(Types.VARIABLE, token);
            }
        } catch (IOException e) {
            System.out.println("Error while lexing variable/keyword: " + e);
            return new Lexeme(Types.UNKNOWN);
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