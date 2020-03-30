import java.io.IOException;

class LexerTest {

    private static final String srcFile = "../TestInput/test1.txt";

    public static void main(String[] args) {
        Lexer lexer = new Lexer(srcFile);
        try {
            Lexeme thisLexeme = lexer.lex();
            while (thisLexeme.getType() != Types.END_OF_INPUT && thisLexeme.getType() != Types.UNKNOWN) {
                System.out.println(thisLexeme);
                thisLexeme = lexer.lex();
            }
            System.out.println("Done lexing");
        } catch (IOException e) {
            System.out.println("Error while lexing: " + e);
        }
    }

}