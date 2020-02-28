import java.io.IOException;

class LexerTest {

    private static final String srcDir = "TestInput";

    public static void main(String[] args) {
        Lexer lexer = new Lexer(srcDir + "/test1.txt");
        try {
            Lexeme thisLexeme = lexer.lex();
            while(thisLexeme.getType() != Types.END_OF_INPUT) {
                System.out.println(thisLexeme);
                thisLexeme = lexer.lex();
            }
            System.out.println("Done lexing");
        } catch (IOException e) {
            System.out.println("Error while lexing: " + e);
        }
    }

}