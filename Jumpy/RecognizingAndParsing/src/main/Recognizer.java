import java.io.IOException;

public class Recognizer {
    private Lexer lexer;
    Lexeme currentLexeme;
    private int line = 1;
    Recognizer (String sourcePath) throws IOException {
        lexer = new Lexer(sourcePath);
        advance();
    }

    public void program() throws IOException {
        optNewLines();
        statementList();
    }

    public void optNewLines() throws IOException {
        if (check(Types.NEWLINE)) {
            match(Types.NEWLINE);
            optNewLines();
        }
    }

    public void statementList() throws IOException {
        optTabs();
        statement();
        if (newLinesPending()) {
            newLines();
            statementList();
        }
    }

    public void optTabs() throws IOException {
        if (check(Types.TAB)) {
            match(Types.TAB);
            optNewLines();
        }
    }

    public void statement() throws IOException {
        if (variableDeclarationPending()) {
            variableDeclaration();
        } else if (variableAssignmentOrMarkerPending()) {
            variableAssignmentOrMarker();
        } else if (jumpStatementPending()) {
            jumpStatement();
        } else {
            printStatement();
        }
    }

    public void newLines() throws IOException {
        match(Types.NEWLINE);
        if (newLinesPending()) {
            newLines();
        }
    }

    public boolean newLinesPending() throws IOException {
        return check(Types.NEWLINE);
    }

    public void variableDeclaration() throws IOException {
        variableType();
        variableAssignmentOrMarker();
    }

    public boolean variableDeclarationPending() throws IOException {
        return variableTypePending();
    }

    public void variableAssignmentOrMarker() throws IOException {
        match(Types.VARIABLE);
        if (check(Types.EQUALS)) {
            match(Types.EQUALS);
            if (arrayInitPending()) {
                arrayInit();
            } else {
                expression();
            }
        } else if (check(Types.AT)) {
            match(Types.AT);
            expression();
            match(Types.EQUALS);
            expression();
        } else {
            match(Types.COLON);
        }
    }

    public boolean variableAssignmentOrMarkerPending() throws IOException {
        return check (Types.VARIABLE);
    }

    /*public void marker() throws IOException {
        match(Types.VARIABLE);
        match(Types.COLON);
    }

    public boolean markerPending() throws IOException {
        return check(Types.VARIABLE);
    }*/

    public void jumpStatement() throws IOException {
        match(Types.JMP);
        expression();
    }

    public boolean jumpStatementPending() throws IOException {
        return check(Types.JMP);
    }

    public void printStatement() throws IOException {
        match(Types.PRINTLN);
        expression();
    }

    public void variableType() throws IOException {
        primitiveType();
        if (check(Types.OPENCURLY)) {
            match(Types.OPENCURLY);
            match(Types.CLOSECURLY);
        }
    }

    public boolean variableTypePending() throws IOException {
        return primitiveTypePending();
    }

    public void primitiveType() throws IOException {
        if (check(Types.INTTYPE)) {
            match(Types.INTTYPE);
        } else if (check(Types.STRINGTYPE)) {
            match(Types.STRINGTYPE);
        } else if (check(Types.FLOATTYPE)) {
            match(Types.FLOATTYPE);
        } else {
            match(Types.BOOLTYPE);
        }
    }

    public boolean primitiveTypePending() throws IOException {
        return check(Types.INTTYPE) || check(Types.STRINGTYPE) ||check(Types.FLOATTYPE) ||check(Types.BOOLTYPE);
    }

    /*public void arrayType() throws IOException {
        primitiveType();
        match(Types.OPENCURLY);
        match(Types.CLOSECURLY);
    }

    public boolean arrayTypePending() throws IOException {
        return primitiveTypePending();
    }*/

    public void arrayInit() throws IOException {
        primitiveType();
        match(Types.OPENCURLY);
        expression();
        match(Types.CLOSECURLY);
    }

    public boolean arrayInitPending() throws IOException {
        return primitiveTypePending();
    }

    public void expression() throws IOException {
        if (ternaryExpressionPending()) {
            ternaryExpression();
        } else {
            unary();
            if (operatorPending()) {
                operator();
                expression();
            }
        }
    }

    public void ternaryExpression() throws IOException {
        match(Types.OSQUARE);
        expression();
        match(Types.QUESTIONMARK);
        expression();
        match(Types.COLON);
        expression();
        match(Types.CSQUARE);
    }

    public boolean ternaryExpressionPending() throws IOException {
        return check(Types.OSQUARE);
    }

    public void unary() throws IOException {
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
    }

    public void operator() throws IOException {
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
    }

    public boolean operatorPending() {
        return check(Types.PLUS) || check(Types.MINUS) ||check(Types.TIMES) ||check(Types.DIVIDE) ||check(Types.MODULO) ||
        check(Types.EQUALTO) || check(Types.GREATERTHAN) || check(Types.LESSTHAN) || check(Types.AND) || check(Types.OR) ||
                check(Types.NOT) || check(Types.AT);
    }

    // helper functions
    boolean check(Types type) {
        return currentLexeme.getType() == type;
    }

    void advance() throws IOException {
        currentLexeme = lexer.lex();
        if (currentLexeme.getType() == Types.NEWLINE) {
            line++;
        }
    }

    void match(Types type) throws IOException {
        matchNoAdvance(type);
        advance();
    }

    void matchNoAdvance(Types type) throws IOException {
        if (!check(type)) {
            throw new IOException("Line " + line + ": expected " + type + " but found " + currentLexeme.getType());
        }
    }

}