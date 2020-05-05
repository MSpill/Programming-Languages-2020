import java.io.IOException;

public class Recognizer {
    private Lexer lexer;
    private Lexeme currentLexeme;
    private Lexeme nextLexeme;
    private String sourcePath;
    private String currentNonTerm;
    Recognizer (String sourcePath) throws IOException {
        this.sourcePath = sourcePath;
        lexer = new Lexer(sourcePath);
        advance();
        advance();
        currentNonTerm = "program";
    }

    public void run() throws IOException {
        System.out.println("Recognizing " + sourcePath);
        program();
    }

    private void program() throws IOException {
        optNewLines();
        optStatementList();
    }

    private void optNewLines() throws IOException {
        if (check(Types.NEWLINE)) {
            match(Types.NEWLINE);
            optNewLines();
        }
    }

    private void optStatementList() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "statements";
        if (check(Types.END_OF_INPUT)) {
            match(Types.END_OF_INPUT);
        } else {
            optTabs();
            statement();
            if (!check(Types.END_OF_INPUT)) {
                newLines();
                optStatementList();
            }
        }
        currentNonTerm = parentNonTerm;
    }

    private void optTabs() throws IOException {
        if (check(Types.TAB)) {
            match(Types.TAB);
            optTabs();
        }
    }

    private Lexeme statement() throws IOException {
        Lexeme myLexeme = null;
        if (variableDeclarationPending()) {
            myLexeme = variableDeclaration();
        } else if (variableAssignmentPending()) {
            myLexeme = variableAssignment();
        } else if (arrayIndexAssignmentPending()) {
            myLexeme = arrayIndexAssignment();
        } else if (markerPending()) {
            myLexeme = marker();
        } else if (jumpStatementPending()) {
            myLexeme = jumpStatement();
        } else {
            myLexeme = printStatement();
        }
        return myLexeme;
    }

    private boolean statementPending() throws IOException {
        return variableDeclarationPending() || variableAssignmentPending() || arrayIndexAssignmentPending() || markerPending() ||
                jumpStatementPending() || printStatementPending();
    }

    private void newLines() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "newlines";
        match(Types.NEWLINE);
        if (newLinesPending()) {
            newLines();
        }
        currentNonTerm = parentNonTerm;
    }

    private boolean newLinesPending() throws IOException {
        return check(Types.NEWLINE);
    }

    private Lexeme variableDeclaration() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "variable declaration";
        Lexeme variableDec = new Lexeme(Types.VARIABLEDECLARATION);
        Lexeme varType = variableType();
        Lexeme varAssign = variableAssignment();
        variableDec.setLeft(varType);
        variableDec.setRight(varAssign);
        currentNonTerm = parentNonTerm;
        return variableDec;
    }

    private boolean variableDeclarationPending() throws IOException {
        return variableTypePending();
    }

    private Lexeme variableAssignment() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "variable assignment";
        Lexeme varName = match(Types.VARIABLE);
        Lexeme eqSign = match(Types.EQUALS);
        eqSign.setLeft(varName);
        if (arrayInitPending()) {
            eqSign.setRight(arrayInit());
        } else {
            eqSign.setRight(expression());
        }
        currentNonTerm = parentNonTerm;
        return eqSign;
    }

    private boolean variableAssignmentPending() throws IOException {
        return check (Types.VARIABLE) && !check2(Types.COLON) && !check2(Types.AT);
    }

    private Lexeme arrayIndexAssignment() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "array index assignment";
        Lexeme parent = new Lexeme(Types.ARRAYINDEXASSIGNMENT);
        parent.setLeft(match(Types.VARIABLE));
        match(Types.AT);
        Lexeme rightGlue = new Lexeme(Types.GLUE);
        rightGlue.setLeft(expression());
        match(Types.EQUALS);
        rightGlue.setRight(expression());
        parent.setRight(rightGlue);
        currentNonTerm = parentNonTerm;
        return parent;
    }

    private boolean arrayIndexAssignmentPending() throws IOException {
        return check(Types.VARIABLE) && check2(Types.AT);
    }

    private Lexeme marker() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "marker";
        Lexeme parent = new Lexeme(Types.MARKER);
        parent.setLeft(match(Types.VARIABLE));
        match(Types.COLON);
        currentNonTerm = parentNonTerm;
        return parent;
    }

    private boolean markerPending() throws IOException {
        return check(Types.VARIABLE) && check2(Types.COLON);
    }

    private Lexeme jumpStatement() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "jump statement";
        Lexeme parent = match(Types.JMP);
        parent.setLeft(expression());
        currentNonTerm = parentNonTerm;
    }

    private boolean jumpStatementPending() throws IOException {
        return check(Types.JMP);
    }

    private Lexeme printStatement() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "print statement";
        Lexeme parent = match(Types.PRINTLN);
        parent.setLeft(expression());
        currentNonTerm = parentNonTerm;
        return parent;
    }

    private boolean printStatementPending() throws IOException {
        return check(Types.PRINTLN);
    }

    private Lexeme variableType() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "variable type";
        Lexeme myType = null;
        if (arrayTypePending()) {
            myType = arrayType();
        } else {
            myType = primitiveType();
        }
        currentNonTerm = parentNonTerm;
        return myType;
    }

    private boolean variableTypePending() throws IOException {
        return primitiveTypePending();
    }

    private Lexeme arrayType() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "array type";
        Lexeme myLexeme = new Lexeme(Types.ARRAYTYPE);
        myLexeme.setLeft(primitiveType());
        match(Types.OPENCURLY);
        match(Types.CLOSECURLY);
        currentNonTerm = parentNonTerm;
        return myLexeme;
    }

    private boolean arrayTypePending() throws IOException {
        return primitiveTypePending() && check2(Types.OPENCURLY);
    }

    private Lexeme primitiveType() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "primitive variable type";
        Lexeme myLexeme;
        if (check(Types.INTTYPE)) {
            myLexeme =  match(Types.INTTYPE);
        } else if (check(Types.STRINGTYPE)) {
            myLexeme =  match(Types.STRINGTYPE);
        } else if (check(Types.FLOATTYPE)) {
            myLexeme =  match(Types.FLOATTYPE);
        } else {
            myLexeme =  match(Types.BOOLTYPE);
        }
        currentNonTerm = parentNonTerm;
        return myLexeme;
    }

    private boolean primitiveTypePending() throws IOException {
        return check(Types.INTTYPE) || check(Types.STRINGTYPE) ||check(Types.FLOATTYPE) ||check(Types.BOOLTYPE);
    }

    private Lexeme arrayInit() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "array initialization";
        Lexeme arrInit = new Lexeme(Types.ARRAYINIT);
        arrInit.setLeft(primitiveType());
        match(Types.OPENCURLY);
        arrInit.setRight(expression());
        match(Types.CLOSECURLY);
        currentNonTerm = parentNonTerm;
        return arrInit;
    }

    private boolean arrayInitPending() throws IOException {
        return primitiveTypePending();
    }

    private Lexeme expression() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "expression";
        Lexeme parent = null;
        if (ternaryExpressionPending()) {
            parent = ternaryExpression();
        } else {
            parent = unary();
            if (operatorPending()) {
                Lexeme tempForUnary = parent;
                parent = operator();
                Lexeme exp = expression();
                parent.setLeft(tempForUnary);
                parent.setRight(exp);
            }
        }
        currentNonTerm = parentNonTerm;
        return parent;
    }

    private Lexeme ternaryExpression() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "ternary expression";
        Lexeme parent = new Lexeme (Types.TERNARYEXPRESSION);
        match(Types.OSQUARE);
        parent.setLeft(expression());
        match(Types.QUESTIONMARK);
        Lexeme rightChild = new Lexeme(Types.GLUE);
        rightChild.setLeft(expression());
        match(Types.COLON);
        rightChild.setRight(expression());
        match(Types.CSQUARE);
        parent.setRight(rightChild);
        currentNonTerm = parentNonTerm;
        return parent;
    }

    private boolean ternaryExpressionPending() throws IOException {
        return check(Types.OSQUARE);
    }

    private Lexeme unary() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "unary";
        Lexeme myLexeme = null;
        if (check(Types.VARIABLE)) {
            myLexeme = match(Types.VARIABLE);
        } else if (check(Types.INTEGER)) {
            myLexeme = match(Types.INTEGER);
        } else if (check(Types.FLOAT)) {
            myLexeme = match(Types.FLOAT);
        } else if (check(Types.STRING)) {
            myLexeme = match(Types.STRING);
        } else if (check(Types.BOOLEAN)) {
            myLexeme = match(Types.BOOLEAN);
        } else if (check(Types.MINUS)) {
            myLexeme = match(Types.MINUS);
            myLexeme.setLeft(unary());
        } else {
            match(Types.OPAREN);
            myLexeme = expression();
            match(Types.CPAREN);
        }
        currentNonTerm = parentNonTerm;
        return myLexeme;
    }

    private Lexeme operator() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "operator";
        Lexeme myLexeme = null;
        if (check(Types.PLUS)) {
            myLexeme = match(Types.PLUS);
        } else if (check(Types.MINUS)) {
            myLexeme = match(Types.MINUS);
        } else if (check(Types.TIMES)) {
            myLexeme = match(Types.TIMES);
        } else if (check(Types.DIVIDE)) {
            myLexeme = match(Types.DIVIDE);
        } else if (check(Types.MODULO)) {
            myLexeme = match(Types.MODULO);
        } else if (check(Types.EQUALTO)) {
            myLexeme = match(Types.EQUALTO);
        } else if (check(Types.GREATERTHAN)) {
            myLexeme = match(Types.GREATERTHAN);
        } else if (check(Types.LESSTHAN)) {
            myLexeme = match(Types.LESSTHAN);
        } else if (check(Types.AND)) {
            myLexeme = match(Types.AND);
        } else if (check(Types.OR)) {
            myLexeme = match(Types.OR);
        } else if (check(Types.NOT)) {
            myLexeme = match(Types.NOT);
        } else {
            myLexeme = match(Types.AT);
        }
        currentNonTerm = parentNonTerm;
        return myLexeme;
    }

    private boolean operatorPending() {
        return check(Types.PLUS) || check(Types.MINUS) ||check(Types.TIMES) ||check(Types.DIVIDE) ||check(Types.MODULO) ||
        check(Types.EQUALTO) || check(Types.GREATERTHAN) || check(Types.LESSTHAN) || check(Types.AND) || check(Types.OR) ||
                check(Types.NOT) || check(Types.AT);
    }

    // helper functions
    private boolean check(Types type) {
        return currentLexeme.getType() == type;
    }

    // function checks 2 lexemes ahead
    private boolean check2(Types type) {
        return nextLexeme.getType() == type;
    }

    private void advance() throws IOException {
        if (nextLexeme != null) {
            currentLexeme = nextLexeme;
        }
        nextLexeme = lexer.lex();
    }

    private Lexeme match(Types type) throws IOException {
        Lexeme tempLexeme = currentLexeme;
        matchNoAdvance(type);
        advance();
        return tempLexeme;
    }

    private void matchNoAdvance(Types type) throws IOException {
        if (!check(type)) {
            throw new IOException("Line " + lexer.getLine() + ": expected " + type + " but found " + currentLexeme.getType() + " while recognizing " + currentNonTerm);
        }
    }

}