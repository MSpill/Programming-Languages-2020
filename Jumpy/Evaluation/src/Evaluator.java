public class Evaluator {

    private int commandNum = 0;

    public int getCommandNum() {
        return commandNum;
    }

    public void run(Lexeme tree) {
        Environments env = new Environments();
        addMarkers(tree, env);
        while (tree != null) {
            if (tree.getLeft().getType() == Types.JMP) {
                Lexeme expression = eval(tree.getLeft().getLeft(), env);
                if (expression.getType() == Types.STRING) {
                    Lexeme jmpCommand = env.lookup(new Lexeme(Types.VARIABLE, expression.getStringVal()));
                    tree = jmpCommand.getRight();
                    commandNum = jmpCommand.getLeft().getIntVal();
                } else if (expression.getType() == Types.INTEGER) {
                    tree = jumpByInteger(tree, expression.getIntVal());
                    commandNum -= expression.getIntVal();
                } else {
                    System.out.println("Only valid inputs to jmp are str and int");
                }
            } else {
                commandNum++;
                if (tree.getLeft().getType() != Types.MARKER) {
                    eval(tree.getLeft(), env);
                }
                tree = tree.getRight();
            }
        }
    }

    private void addMarkers(Lexeme tree, Environments env) {
        int currCommand = 0;
        while (tree != null) {
            currCommand++;
            if (tree.getType() == Types.STATEMENTLIST) {
                if (tree.getLeft().getType() == Types.MARKER) {
                    // variable name is marker name, value is lexeme w/ right child as parse tree of all subsequent statements
                    Lexeme valInsert = new Lexeme(Types.GLUE);
                    valInsert.setLeft(new Lexeme(Types.INTEGER, currCommand));
                    valInsert.setRight(tree.getRight());
                    env.insert(tree.getLeft().getLeft(), valInsert);
                }
                tree = tree.getRight();
            }
        }
    }

    private Lexeme jumpByInteger(Lexeme statement, int jump) {
        int i = jump;
        while (i != 0) {
            if (i < 0) {
                statement = statement.getParent();
                i++;
            } else {
                statement = statement.getRight();
                i--;
            }
            if (statement == null) {
                System.out.println("Command " + commandNum + " - Jump statement tried to jump outside of program");
                return null;
            }
        }
        return statement;
    }

    private Lexeme eval(Lexeme tree, Environments env) {
        if (tree == null) {
            return null;
        }
        switch (tree.getType()) {

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
            case ARRAYINIT: return evalArrayInit(tree, env);
            case EQUALS: return evalVariableAssignment(tree, env);
            case ARRAYINDEXASSIGNMENT: return evalIndexAssignment(tree, env);

            case PRINTLN: return evalPrintln(tree, env);

            // already handled in addMarkers
            // case MARKER: return evalMarker(tree, env);

            // handled separately in run()
            // case JMP: return evalJumpStatement(tree, env);

        }
        return null;
    }

    private Lexeme evalTernary(Lexeme tree, Environments env) {
        Lexeme condition = eval(tree.getLeft(), env);
        if (condition.getBoolVal()) {
            return eval(tree.getRight().getLeft(), env);
        } else {
            return eval(tree.getRight().getRight(), env);
        }
    }

    private Lexeme evalShortCircuitOp(Lexeme tree, Environments env) {
        if (tree.getType() == Types.AND) {
            Lexeme left = eval(tree.getLeft(), env);
            if (!left.getBoolVal()) {
                return new Lexeme(Types.BOOLEAN, false);
            } else {
                Lexeme right = eval(tree.getRight(), env);
                if (right.getBoolVal()) {
                    return new Lexeme(Types.BOOLEAN, true);
                }
                return new Lexeme(Types.BOOLEAN, false);
            }
        } else {
            Lexeme left = eval(tree.getLeft(), env);
            if (left.getBoolVal()) {
                return new Lexeme(Types.BOOLEAN, true);
            }
            Lexeme right = eval(tree.getRight(), env);
            if (right.getBoolVal()) {
                return new Lexeme(Types.BOOLEAN, true);
            }
            return new Lexeme(Types.BOOLEAN, false);
        }
    }

    private Lexeme evalVariableDeclaration(Lexeme tree, Environments env) {
        Types varType = tree.getLeft().getType();
        Lexeme variable = tree.getRight().getLeft();
        Lexeme value = eval(tree.getRight().getRight(), env);
        if (varType == Types.ARRAYTYPE) {
            varType = tree.getLeft().getLeft().getType();
        }
        if (value.getType() == Types.INTEGER && varType != Types.INTTYPE
                || value.getType() == Types.FLOAT && varType != Types.FLOATTYPE
                || value.getType() == Types.STRING && varType != Types.STRINGTYPE
                || value.getType() == Types.BOOLEAN && varType != Types.BOOLTYPE) {
            System.out.println(variable + ": declared type does not match value");
            return null;
        }
        env.insert(variable, value);
        return value;
    }

    private Lexeme evalArrayInit(Lexeme tree, Environments env) {
        Types type = tree.getLeft().getType();
        int length = eval(tree.getRight(), env).getIntVal();
        switch (type) {
            case STRINGTYPE: return new Lexeme(Types.STRINGARR, new String[length]);
            case INTTYPE: return new Lexeme(Types.INTARR, new Integer[length]);
            case BOOLTYPE: return new Lexeme(Types.BOOLARR, new Boolean[length]);
            case FLOATTYPE: return new Lexeme(Types.FLOATARR, new Double[length]);
        }
        return null;
    }

    private Lexeme evalVariableAssignment(Lexeme tree, Environments env) {
        Lexeme variable = tree.getLeft();
        Lexeme value = eval(tree.getRight(), env);
        if (env.lookup(variable) != null) {
            env.insert(variable, value);
            return value;
        } else {
            System.out.println("Error during variable assignment: variable " + variable.getStringVal() + " is not defined");
            return null;
        }
    }

    private Lexeme evalIndexAssignment(Lexeme tree, Environments env) {
        Lexeme variable = tree.getLeft();
        int index = eval(tree.getRight().getLeft(), env).getIntVal();
        Lexeme value = eval(tree.getRight().getRight(), env);
        Lexeme currArr = env.lookup(variable);
        switch (currArr.getType()) {
            case STRINGARR:
                String[] prevArr = currArr.getStringArr();
                prevArr[index] = value.getStringVal();
                currArr.setStringArr(prevArr);
                break;
            case INTARR:
                Integer[] prevArrInt = currArr.getIntArr();
                prevArrInt[index] = value.getIntVal();
                currArr.setIntArr(prevArrInt);
                break;
            case BOOLARR:
                Boolean[] prevArrBool = currArr.getBoolArr();
                prevArrBool[index] = value.getBoolVal();
                currArr.setBoolArr(prevArrBool);
                break;
            case FLOATARR:
                Double[] prevArrFloat = currArr.getRealArr();
                prevArrFloat[index] = value.getDoubleVal();
                currArr.setRealArr(prevArrFloat);
                break;
        }
        return value;
    }

    private Lexeme evalPrintln(Lexeme tree, Environments env) {
        Lexeme out = eval(tree.getLeft(), env);
        String outStr = null;
        if (out.getType() == Types.INTEGER) {
            outStr = out.getIntVal() + "";
        } else if (out.getType() == Types.FLOAT) {
            outStr = out.getDoubleVal() + "";
        } else if (out.getType() == Types.STRING) {
            outStr = out.getStringVal() + "";
        } else if (out.getType() == Types.BOOLEAN) {
            outStr = out.getBoolVal() + "";
        } else {
            outStr = formatArr(out);
        }
        System.out.println(outStr);
        return out;
    }

    private String formatArr(Lexeme arr) {
        Object[] objArr = arr.getAnyArr();
        String out = arr.getType() + " {";
        for (int i = 0; i < objArr.length; i++) {
            out += objArr[i];
            if ( i < objArr.length - 1) {
                out += ", ";
            }
        }
        return out + "}";
    }

    private Lexeme evalJumpStatement(Lexeme tree, Environments env) {
        Lexeme markerName = eval(tree.getLeft(), env);
        return eval(env.lookup(new Lexeme(Types.VARIABLE, markerName.getStringVal())), env);
    }

    private Lexeme evalSimpleOp(Lexeme tree, Environments env) {
        switch (tree.getType()) {
            case PLUS: return evalPlus(tree, env);
            case MINUS: return evalMinus(tree, env);
            case TIMES: return evalTimes(tree, env);
            case DIVIDE: return evalDivide(tree, env);
            case MODULO: return evalModulo(tree, env);
            case GREATERTHAN: return evalGreaterThan(tree, env);
            case LESSTHAN: return evalLessThan(tree, env);
            case NOT: return evalNot(tree, env);
            case AT: return evalAt(tree, env);
            case EQUALTO: return evalEqualTo(tree, env);
            // implement others
        }
        return null;
    }

    private Lexeme evalPlus(Lexeme tree, Environments env) {
        Lexeme left = eval(tree.getLeft(), env);
        Lexeme right = eval(tree.getRight(), env);
        if (left.getType() == Types.INTEGER && right.getType() == Types.INTEGER) {
            return new Lexeme(Types.INTEGER, left.getIntVal() + right.getIntVal());
        } else {
            return new Lexeme(Types.FLOAT, left.getDoubleVal() + right.getDoubleVal());
        }
    }

    private Lexeme evalMinus(Lexeme tree, Environments env) {
        Lexeme left = eval(tree.getLeft(), env);
        Lexeme right = eval(tree.getRight(), env);
        if (right == null) {
            if (left.getType() == Types.INTEGER) {
                return new Lexeme(Types.INTEGER, -left.getIntVal());
            } else {
                return new Lexeme(Types.FLOAT, -left.getDoubleVal());
            }
        } else {
            if (left.getType() == Types.INTEGER && right.getType() == Types.INTEGER) {
                return new Lexeme(Types.INTEGER, left.getIntVal() - right.getIntVal());
            } else {
                return new Lexeme(Types.FLOAT, left.getDoubleVal() - right.getDoubleVal());
            }
        }
    }

    private Lexeme evalTimes(Lexeme tree, Environments env) {
        Lexeme left = eval(tree.getLeft(), env);
        Lexeme right = eval(tree.getRight(), env);
        if (left.getType() == Types.INTEGER && right.getType() == Types.INTEGER) {
            return new Lexeme(Types.INTEGER, left.getIntVal() * right.getIntVal());
        } else {
            return new Lexeme(Types.FLOAT, left.getDoubleVal() * right.getDoubleVal());
        }
    }

    private Lexeme evalDivide(Lexeme tree, Environments env) {
        Lexeme left = eval(tree.getLeft(), env);
        Lexeme right = eval(tree.getRight(), env);
        if (left.getType() == Types.INTEGER && right.getType() == Types.INTEGER) {
            return new Lexeme(Types.INTEGER, left.getIntVal() / right.getIntVal());
        } else {
            return new Lexeme(Types.FLOAT, left.getDoubleVal() / right.getDoubleVal());
        }
    }

    private Lexeme evalModulo(Lexeme tree, Environments env) {
        Lexeme left = eval(tree.getLeft(), env);
        Lexeme right = eval(tree.getRight(), env);
        if (left.getType() == Types.INTEGER && right.getType() == Types.INTEGER) {
            return new Lexeme(Types.INTEGER, left.getIntVal() % right.getIntVal());
        } else {
            return new Lexeme(Types.FLOAT, left.getDoubleVal() % right.getDoubleVal());
        }
    }

    private Lexeme evalGreaterThan(Lexeme tree, Environments env) {
        Lexeme left = eval(tree.getLeft(), env);
        Lexeme right = eval(tree.getRight(), env);
        return new Lexeme(Types.BOOLEAN, left.getDoubleVal() > right.getDoubleVal()); // getDoubleVal() uses intVal if present
    }

    private Lexeme evalLessThan(Lexeme tree, Environments env) {
        Lexeme left = eval(tree.getLeft(), env);
        Lexeme right = eval(tree.getRight(), env);
        return new Lexeme(Types.BOOLEAN, left.getDoubleVal() < right.getDoubleVal()); // getDoubleVal() uses intVal if present
    }

    private Lexeme evalEqualTo(Lexeme tree, Environments env) {
        Lexeme left = eval(tree.getLeft(), env);
        Lexeme right = eval(tree.getRight(), env);
        return new Lexeme(Types.BOOLEAN, left.getDoubleVal() == right.getDoubleVal()); // getDoubleVal() uses intVal if present
    }

    private Lexeme evalNot(Lexeme tree, Environments env) {
        Lexeme left = eval(tree.getLeft(), env);
        return new Lexeme(Types.BOOLEAN, !left.getBoolVal());
    }

    private Lexeme evalAt(Lexeme tree, Environments env) {
        Lexeme arr = eval(tree.getLeft(), env);
        int index = eval(tree.getRight(), env).getIntVal();
        switch (arr.getType()) {
            case STRINGARR: return new Lexeme(Types.STRING, (arr.getStringArr())[index]);
            case INTARR: return new Lexeme(Types.INTEGER, (arr.getIntArr())[index]);
            case BOOLARR: return new Lexeme(Types.BOOLEAN, (arr.getBoolArr())[index]);
            case FLOATARR: return new Lexeme(Types.FLOAT, (arr.getRealArr())[index]);
        }
        return null;
    }
}
