class Lexeme {
    Type type;
    String string;
    int integer;
    double real;

    Lexeme(Type t) {
        this.type = t;
    }

    Type getType() {
        return this.type;
    }
}

enum Type {
    END_OF_INPUT,
}