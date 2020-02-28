class Lexeme {
    private Types type;
    private String string;
    private Integer integer;
    private Double real;

    Lexeme(Types t) {
        this.type = t;
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