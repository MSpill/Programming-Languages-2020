

public class EnvironmentsTest {

    public static void main(String[] args) {
        Environments myEnv = new Environments();
        myEnv.insert("x", new Lexeme(Types.INTEGER, 10));
        System.out.println(myEnv.lookup("x"));
        myEnv.insert("y", new Lexeme(Types.STRING, "hello"));
        System.out.println(myEnv.lookup("y"));

        myEnv.extendEnv(null, null);
        myEnv.insert("x", new Lexeme(Types.FLOAT, 0.3));
        System.out.println(myEnv.lookup("x"));
        System.out.println(myEnv.lookup("y"));

        myEnv.printTree();
    }

}
