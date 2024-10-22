import java.io.*;

class Lexer {
    private PushbackReader input;
    private int line;

    Lexer(String inputFilePath) {
        line = 1;
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
            skipComments();
            char ch = (char) input.read();
            if (isEOF(ch)) {
                return new Lexeme(Types.END_OF_INPUT);
            }

            switch (ch) {
                case '\n':
                    line++;
                    return new Lexeme(Types.NEWLINE);
                case '\t':
                    return new Lexeme(Types.TAB);
                case '+':
                    return new Lexeme(Types.PLUS);
                case '-':
                    return new Lexeme(Types.MINUS);
                case '*':
                    return new Lexeme(Types.TIMES);
                case '/':
                    return new Lexeme(Types.DIVIDE);
                case '%':
                    return new Lexeme(Types.MODULO);
                case '>':
                    return new Lexeme(Types.GREATERTHAN);
                case '<':
                    return new Lexeme(Types.LESSTHAN);
                case '@':
                    return new Lexeme(Types.AT);
                case '?':
                    return new Lexeme(Types.QUESTIONMARK);
                case '(':
                    return new Lexeme(Types.OPAREN);
                case ')':
                    return new Lexeme(Types.CPAREN);
                case '{':
                    return new Lexeme(Types.OPENCURLY);
                case '}':
                    return new Lexeme(Types.CLOSECURLY);
                case '[':
                    return new Lexeme(Types.OSQUARE);
                case ']':
                    return new Lexeme(Types.CSQUARE);
                case ':':
                    return new Lexeme(Types.COLON);
                default:
                    if (Character.isDigit(ch)) {
                        input.unread(ch);
                        return lexNumber();
                    } else if (Character.isLetter(ch) || ch == '=') {
                        input.unread(ch);
                        return lexVariableOrKeyword();
                    } else if (ch == '\"') {
                        return lexString();
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
            boolean isInteger = true;
            String token = "";
            char ch = (char) input.read();
            while (Character.isDigit(ch) || ch == '.') {
                token = token + ch;
                if (ch == '.') {
                    isInteger = false;
                }
                ch = (char) input.read();
            }
            input.unread(ch);
            if (isInteger) {
                return new Lexeme(Types.INTEGER, Integer.parseInt(token));
            } else {
                return new Lexeme(Types.FLOAT, Double.parseDouble(token));
            }
        } catch (IOException e) {
            System.out.println("Error while lexing number: " + e);
            return new Lexeme(Types.UNKNOWN);
        }
    }

    private Lexeme lexString() {
        try {
            String token = "";
            char ch = (char) input.read();
            if (isEOF(ch)) {
                throw new IOException("Found EOF when expecting end of string");
            }
            if (ch == '\n') {
                throw new IOException("Encountered newline when expecting end of string");
            }
            while (ch != '\"') {
                token = token + ch;
                ch = (char) input.read();
                if (isEOF(ch)) {
                    throw new IOException("Found EOF when expecting end of string");
                }
                if (ch == '\n') {
                    throw new IOException("Encountered newline when expecting end of string");
                }
            }

            return new Lexeme(Types.STRING, token);
        } catch (IOException e) {
            System.out.println("Error while lexing variable/keyword: " + e);
            return new Lexeme(Types.UNKNOWN);
        }
    }

    private Lexeme lexVariableOrKeyword() {
        try {
            String token = "";
            char ch = (char) input.read();
            while (Character.isDigit(ch) || Character.isLetter(ch) || ch == '=') {
                token = token + ch;
                ch = (char) input.read();
            }

            input.unread(ch);

            if (token.equals("jmp")) {
                return new Lexeme(Types.JMP);
            } else if (token.equals("println")) {
                return new Lexeme(Types.PRINTLN);
            } else if (token.equals("int")) {
                return new Lexeme(Types.INTTYPE);
            } else if (token.equals("str")) {
                return new Lexeme(Types.STRINGTYPE);
            } else if (token.equals("float")) {
                return new Lexeme(Types.FLOATTYPE);
            } else if (token.equals("bool")) {
                return new Lexeme(Types.BOOLTYPE);
            } else if (token.equals("true")) {
                return new Lexeme(Types.BOOLEAN, true);
            } else if (token.equals("false")) {
                return new Lexeme(Types.BOOLEAN, false);
            } else if (token.equals("and")) {
                return new Lexeme(Types.AND);
            } else if (token.equals("or")) {
                return new Lexeme(Types.OR);
            } else if (token.equals("not")) {
                return new Lexeme(Types.NOT);
            } else if (token.equals("==")) {
                return new Lexeme(Types.EQUALTO);
            } else if (token.equals("=")) {
                return new Lexeme(Types.EQUALS);
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

    private void skipComments() throws IOException {
        try {
            char ch = (char) input.read();
            if (ch == '|') {
                ch = (char) input.read();
                while (ch != '|') {
                    if (isEOF(ch)) {
                        throw new IOException("Found EOF when expecting end of comment");
                    }
                    if (ch == '\n') {
                        line++;
                    }
                    ch = (char) input.read();
                }
            } else {
                input.unread(ch);
            }
        } catch (IOException e) {
            System.out.println("Error while skipping comment: " + e);
        }
    }

    int getLine() {
        return line;
    }

    private boolean isWhiteSpace(char ch) {
        // newlines and tabs are part of my language's grammar, so aren't skipped
        if (ch == ' ') {
            return true;
        } else {
            return false;
        }
    }

    private boolean isEOF(char ch) {
        int charAsInt = (int) ch;
        if (charAsInt == 65535) { // check for end of file
            return true;
        } else {
            return false;
        }
    }
}