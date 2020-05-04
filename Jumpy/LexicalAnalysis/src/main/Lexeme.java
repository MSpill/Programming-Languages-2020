class Lexeme {
    private Types type;
    private String string;
    private Integer integer;
    private boolean bool;
    private boolean hasBoolVal = false;
    private Double real;
    private Lexeme left, right;

    Lexeme(Types t) {
        this.type = t;
        this.left = null;
        this.right = null;
    }

    Lexeme(Types t, String string) {
        this.type = t;
        this.string = string;
    }

    Lexeme(Types t, int integer) {
        this.type = t;
        this.integer = integer;
    }

    Lexeme(Types t, double real) {
        this.type = t;
        this.real = real;
    }

    Lexeme(Types t, boolean bool) {
        this.type = t;
        this.bool = bool;
        hasBoolVal = true;
    }

    Types getType() {
        return this.type;
    }

    public void setLeft(Lexeme child) {
        this.left = child;
    }

    public void setRight(Lexeme child) {
        this.right = child;
    }

    public String toString() {
        if (string != null) {
            if (getType() == Types.STRING) {
                return getType() + " " + "\"" + string + "\"";
            }
            return getType() + " " + string;
        }
        if (integer != null) {
            return getType() + " " + integer;
        }
        if (real != null) {
            return getType() + " " + real;
        }
        if (hasBoolVal) {
            return getType() + " " + bool;
        }
        return getType() + " ";
    }
}