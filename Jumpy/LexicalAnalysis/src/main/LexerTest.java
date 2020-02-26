import java.io.IOException;

class LexerTest {

    private static final String srcDir = "./Programming-Languages-2020/Jumpy/LexicalAnalysis/src/main/TestInput";

    public static void main(String[] args) {
        Lexer lexer = new Lexer(srcDir + "/test1.txt");
        /*
         * try { Lexeme lexeme = lexer.lex(); while (lexeme.getType() !=
         * Type.END_OF_INPUT) { System.out.println(lexeme); lexeme = lexer.lex(); } }
         * catch (IOException e) { System.out.println("Error while lexing: " + e); }
         */
    }

}