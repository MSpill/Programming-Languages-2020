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

    private void statement() throws IOException {
        if (variableDeclarationPending()) {
            variableDeclaration();
        } else if (variableAssignmentPending()) {
            variableAssignment();
        } else if (arrayIndexAssignmentPending()) {
            arrayIndexAssignment();
        } else if (markerPending()) {
            marker();
        } else if (jumpStatementPending()) {
            jumpStatement();
        } else {
            printStatement();
        }
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

    private void variableDeclaration() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "variable declaration";
        variableType();
        variableAssignment();
        currentNonTerm = parentNonTerm;
    }

    private boolean variableDeclarationPending() throws IOException {
        return variableTypePending();
    }

    private void variableAssignment() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "variable assignment";
        match(Types.VARIABLE);
        match(Types.EQUALS);
        if (arrayInitPending()) {
            arrayInit();
        } else {
            expression();
        }
        currentNonTerm = parentNonTerm;
    }

    private boolean variableAssignmentPending() throws IOException {
        return check (Types.VARIABLE) && !check2(Types.COLON) && !check2(Types.AT);
    }

    private void arrayIndexAssignment() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "array index assignment";
        match(Types.VARIABLE);
        match(Types.AT);
        expression();
        match(Types.EQUALS);
        expression();
        currentNonTerm = parentNonTerm;
    }

    private boolean arrayIndexAssignmentPending() throws IOException {
        return check(Types.VARIABLE) && check2(Types.AT);
    }

    private void marker() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "marker";
        match(Types.VARIABLE);
        match(Types.COLON);
        currentNonTerm = parentNonTerm;
    }

    private boolean markerPending() throws IOException {
        return check(Types.VARIABLE) && check2(Types.COLON);
    }

    private void jumpStatement() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "jump statement";
        match(Types.JMP);
        expression();
        currentNonTerm = parentNonTerm;
    }

    private boolean jumpStatementPending() throws IOException {
        return check(Types.JMP);
    }

    private void printStatement() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "print statement";
        match(Types.PRINTLN);
        expression();
        currentNonTerm = parentNonTerm;
    }

    private boolean printStatementPending() throws IOException {
        return check(Types.PRINTLN);
    }

    private void variableType() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "variable type";
        if (arrayTypePending()) {
            arrayType();
        } else {
            primitiveType();
        }
        currentNonTerm = parentNonTerm;
    }

    private boolean variableTypePending() throws IOException {
        return primitiveTypePending();
    }

    private void arrayType() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "array type";
        primitiveType();
        match(Types.OPENCURLY);
        match(Types.CLOSECURLY);
        currentNonTerm = parentNonTerm;
    }

    private boolean arrayTypePending() throws IOException {
        return primitiveTypePending() && check2(Types.OPENCURLY);
    }

    private void primitiveType() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "primitive variable type";
        if (check(Types.INTTYPE)) {
            match(Types.INTTYPE);
        } else if (check(Types.STRINGTYPE)) {
            match(Types.STRINGTYPE);
        } else if (check(Types.FLOATTYPE)) {
            match(Types.FLOATTYPE);
        } else {
            match(Types.BOOLTYPE);
        }
        currentNonTerm = parentNonTerm;
    }

    private boolean primitiveTypePending() throws IOException {
        return check(Types.INTTYPE) || check(Types.STRINGTYPE) ||check(Types.FLOATTYPE) ||check(Types.BOOLTYPE);
    }

    private void arrayInit() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "array initialization";
        primitiveType();
        match(Types.OPENCURLY);
        expression();
        match(Types.CLOSECURLY);
        currentNonTerm = parentNonTerm;
    }

    private boolean arrayInitPending() throws IOException {
        return primitiveTypePending();
    }

    private void expression() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "expression";
        if (ternaryExpressionPending()) {
            ternaryExpression();
        } else {
            unary();
            if (operatorPending()) {
                operator();
                expression();
            }
        }
        currentNonTerm = parentNonTerm;
    }

    private void ternaryExpression() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "ternary expression";
        match(Types.OSQUARE);
        expression();
        match(Types.QUESTIONMARK);
        expression();
        match(Types.COLON);
        expression();
        match(Types.CSQUARE);
        currentNonTerm = parentNonTerm;
    }

    private boolean ternaryExpressionPending() throws IOException {
        return check(Types.OSQUARE);
    }

    private void unary() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "unary";
        if (check(Types.VARIABLE)) {
            match(Types.VARIABLE);
        } else if (check(Types.INTEGER)) {
            match(Types.INTEGER);
        } else if (check(Types.FLOAT)) {
            match(Types.FLOAT);
        } else if (check(Types.STRING)) {
            match(Types.STRING);
        } else if (check(Types.BOOLEAN)) {
            match(Types.BOOLEAN);
        } else if (check(Types.MINUS)) {
            match(Types.MINUS);
            unary();
        } else {
            match(Types.OPAREN);
            expression();
            match(Types.CPAREN);
        }
        currentNonTerm = parentNonTerm;
    }

    private void operator() throws IOException {
        String parentNonTerm = currentNonTerm;
        currentNonTerm = "operator";
        if (check(Types.PLUS)) {
            match(Types.PLUS);
        } else if (check(Types.MINUS)) {
            match(Types.MINUS);
        } else if (check(Types.TIMES)) {
            match(Types.TIMES);
        } else if (check(Types.DIVIDE)) {
            match(Types.DIVIDE);
        } else if (check(Types.MODULO)) {
            match(Types.MODULO);
        } else if (check(Types.EQUALTO)) {
            match(Types.EQUALTO);
        } else if (check(Types.GREATERTHAN)) {
            match(Types.GREATERTHAN);
        } else if (check(Types.LESSTHAN)) {
            match(Types.LESSTHAN);
        } else if (check(Types.AND)) {
            match(Types.AND);
        } else if (check(Types.OR)) {
            match(Types.OR);
        } else if (check(Types.NOT)) {
            match(Types.NOT);
        } else {
            match(Types.AT);
        }
        currentNonTerm = parentNonTerm;
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

    private void match(Types type) throws IOException {
        matchNoAdvance(type);
        advance();
    }

    private void matchNoAdvance(Types type) throws IOException {
        if (!check(type)) {
            throw new IOException("Line " + lexer.getLine() + ": expected " + type + " but found " + currentLexeme.getType() + " while recognizing " + currentNonTerm);
        }
    }

}