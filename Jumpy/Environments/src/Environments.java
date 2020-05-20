

public class Environments {
    private Lexeme localEnv;
    public Environments() {
        localEnv = new Lexeme(Types.ENV);
        localEnv.setRight(new Lexeme(Types.VALUES));
    }

    public Lexeme lookup(Lexeme variable) {
        Lexeme env = this.localEnv;
        while (env != null) {
            Lexeme vars = env.getLeft();
            Lexeme vals = env.getRight().getLeft();
            while (vars != null) {
                if (vars.getLeft().getStringVal().equals(variable.getStringVal())) {
                    return vals.getLeft();
                }
                vars = vars.getRight();
                vals = vals.getRight();
            }
            env = env.getRight().getRight();
        }
        // throw an exception here
        return null;
    }

    public void insert(Lexeme variable, Lexeme value) {
        Lexeme newVars = new Lexeme(Types.GLUE);
        newVars.setRight(this.localEnv.getLeft());
        newVars.setLeftQuiet(variable);
        Lexeme newVals = new Lexeme(Types.GLUE);
        newVals.setRight(this.localEnv.getRight().getLeft());
        newVals.setLeftQuiet(value);
        this.localEnv.setLeft(newVars);
        this.localEnv.getRight().setLeft(newVals);
    }

    public void extendEnv(Lexeme newVars, Lexeme newVals) {
        Lexeme temp = localEnv;
        localEnv = new Lexeme(Types.ENV);
        localEnv.setLeft(newVars);
        Lexeme newValGlue = new Lexeme(Types.VALUES);
        newValGlue.setLeft(newVals);
        newValGlue.setRight(temp);
        localEnv.setRight(newValGlue);
    }

    public void printTree() {
        localEnv.printTree();
    }
}
