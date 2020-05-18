

public class EnvironmentsTest {

    public static void main(String[] args) {
        Environments myEnv = new Environments();
        myEnv.insert(new Lexeme(Types.VARIABLE, "x"), new Lexeme(Types.INTEGER, 10));
        System.out.println(myEnv.lookup(new Lexeme(Types.VARIABLE, "x")));
        myEnv.insert(new Lexeme(Types.VARIABLE, "y"), new Lexeme(Types.STRING, "hello"));
        System.out.println(myEnv.lookup(new Lexeme(Types.VARIABLE, "y")));

        myEnv.extendEnv(null, null);
        myEnv.insert(new Lexeme(Types.VARIABLE, "x"), new Lexeme(Types.FLOAT, 0.3));
        System.out.println(myEnv.lookup(new Lexeme(Types.VARIABLE, "x")));
        System.out.println(myEnv.lookup(new Lexeme(Types.VARIABLE, "y")));

        myEnv.printTree();
    }

}
