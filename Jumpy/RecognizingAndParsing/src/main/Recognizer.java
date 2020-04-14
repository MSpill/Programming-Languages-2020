import java.io.IOException;

public class Recognizer {
    private Lexer lexer;
    private Lexeme currentLexeme;
    private Lexeme nextLexeme;
    private String sourcePath;
    Recognizer (String sourcePath) throws IOException {
        this.sourcePath = sourcePath;
        lexer = new Lexer(sourcePath);
        advance();
        advance();
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
        match(Types.NEWLINE);
        if (newLinesPending()) {
            newLines();
        }
    }

    private boolean newLinesPending() throws IOException {
        return check(Types.NEWLINE);
    }

    private void variableDeclaration() throws IOException {
        variableType();
        variableAssignment();
    }

    private boolean variableDeclarationPending() throws IOException {
        return variableTypePending();
    }

    private void variableAssignment() throws IOException {
        match(Types.VARIABLE);
        match(Types.EQUALS);
        if (arrayInitPending()) {
            arrayInit();
        } else {
            expression();
        }
    }

    private boolean variableAssignmentPending() throws IOException {
        return check (Types.VARIABLE) && !check2(Types.COLON) && !check2(Types.AT);
    }

    private void arrayIndexAssignment() throws IOException {
        match(Types.VARIABLE);
        match(Types.AT);
        expression();
        match(Types.EQUALS);
        expression();
    }

    private boolean arrayIndexAssignmentPending() throws IOException {
        return check(Types.VARIABLE) && check2(Types.AT);
    }

    private void marker() throws IOException {
        match(Types.VARIABLE);
        match(Types.COLON);
    }

    private boolean markerPending() throws IOException {
        return check(Types.VARIABLE) && check2(Types.COLON);
    }

    private void jumpStatement() throws IOException {
        match(Types.JMP);
        expression();
    }

    private boolean jumpStatementPending() throws IOException {
        return check(Types.JMP);
    }

    private void printStatement() throws IOException {
        match(Types.PRINTLN);
        expression();
    }

    private boolean printStatementPending() throws IOException {
        return check(Types.PRINTLN);
    }

    private void variableType() throws IOException {
        if (arrayTypePending()) {
            arrayType();
        } else {
            primitiveType();
        }
    }

    private boolean variableTypePending() throws IOException {
        return primitiveTypePending();
    }

    private void arrayType() throws IOException {
        primitiveType();
        match(Types.OPENCURLY);
        match(Types.CLOSECURLY);
    }

    private boolean arrayTypePending() throws IOException {
        return primitiveTypePending() && check2(Types.OPENCURLY);
    }

    private void primitiveType() throws IOException {
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

    private boolean primitiveTypePending() throws IOException {
        return check(Types.INTTYPE) || check(Types.STRINGTYPE) ||check(Types.FLOATTYPE) ||check(Types.BOOLTYPE);
    }

    private void arrayInit() throws IOException {
        primitiveType();
        match(Types.OPENCURLY);
        expression();
        match(Types.CLOSECURLY);
    }

    private boolean arrayInitPending() throws IOException {
        return primitiveTypePending();
    }

    private void expression() throws IOException {
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

    private void ternaryExpression() throws IOException {
        match(Types.OSQUARE);
        expression();
        match(Types.QUESTIONMARK);
        expression();
        match(Types.COLON);
        expression();
        match(Types.CSQUARE);
    }

    private boolean ternaryExpressionPending() throws IOException {
        return check(Types.OSQUARE);
    }

    private void unary() throws IOException {
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

    private void operator() throws IOException {
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
            throw new IOException("Line " + lexer.getLine() + ": expected " + type + " but found " + currentLexeme.getType());
        }
    }

}