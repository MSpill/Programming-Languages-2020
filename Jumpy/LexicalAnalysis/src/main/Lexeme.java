class Lexeme {
    private Types type;
    private String string;
    private Integer integer;
    private boolean bool;
    private boolean hasBoolVal = false;
    private Double real;

    Lexeme(Types t) {
        this.type = t;
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