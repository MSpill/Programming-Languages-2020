class Lexeme {
    private Types type;
    private String string;
    private Integer integer;
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
        this.integer = new Integer(integer);
    }

    Lexeme(Types t, double real) {
        this.type = t;
        this.real = new Double(real);
    }

    Types getType() {
        return this.type;
    }

    public String toString() {
        if (string != null) {
            return getType() + " " + string;
        }
        if (integer != null) {
            return getType() + " " + integer;
        }
        if (real != null) {
            return getType() + " " + real;
        }
        return getType() + " ";
    }
}