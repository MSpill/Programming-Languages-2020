public class Evaluator {

    public void run(Lexeme tree) {
        Environments env = new Environments();
        eval(tree, env);
    }

    public Lexeme eval(Lexeme tree, Environments env) {

        switch (tree.getType()) {

            case STATEMENTLIST:
                eval(tree.getLeft(), env);
                return eval(tree.getRight(), env);

            case INTEGER:
            case FLOAT:
            case STRING:
            case BOOLEAN: return tree;

            case VARIABLE: return env.lookup(tree);

            case PLUS:
            case MINUS:
            case TIMES:
            case DIVIDE:
            case MODULO:
            case GREATERTHAN:
            case LESSTHAN:
            case NOT:
            case AT:
            case EQUALTO: return evalSimpleOp(tree, env);

            case TERNARYEXPRESSION: return evalTernary(tree, env);

            case AND:
            case OR: return evalShortCircuitOp(tree, env);

            case VARIABLEDECLARATION: return evalVariableDeclaration(tree, env);

            case PRINTLN: return evalPrintln(tree, env);

            case MARKER: return evalMarker(tree, env);

            case JMP: return evalJumpStatement(tree, env);

        }
        return null;
    }

}
