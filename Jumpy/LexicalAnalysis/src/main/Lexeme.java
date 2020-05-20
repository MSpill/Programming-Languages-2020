class Lexeme {
    private Types type;
    private String string;
    private Integer integer;
    private boolean bool;
    private boolean hasBoolVal = false;
    private Double real;
    private Lexeme left, right, parent;

    Lexeme(Types t) {
        this.type = t;
        this.left = null;
        this.right = null;
        this.parent = null;
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
        if (child != null) {
            child.setParent(this);
        }
        this.left = child;
    }

    public void setRight(Lexeme child) {
        if (child != null) {
            child.setParent(this);
        }
        this.right = child;
    }

    public void setParent(Lexeme par) {
        this.parent = par;
    }

    public Lexeme getLeft() {
        return this.left;
    }

    public Lexeme getRight() {
        return this.right;
    }

    public Lexeme getParent() {
        return this.parent;
    }

    public String getStringVal() {
        return this.string;
    }

    public int getIntVal() {
        return this.integer;
    }

    public double getDoubleVal() {
        if (real != null) {
            return real;
        } else {
            return integer + 0.0;
        }
    }

    public boolean getBoolVal() {
        if (hasBoolVal) {
            return bool;
        } else {
            System.out.println("Requested bool val from non-bool lexeme");
            return false;
        }
    }

    public void printTree() {
        this.printTreeParams(0, 0);
    }

    public void printTreeParams(int depth, int posFlag) {
        String indent = "";
        for (int i = 0; i < depth; i++) {
            indent+=" ";
        }
        if (posFlag == -1) {
            System.out.println(indent + "with left child: " + this);
        } else if (posFlag == 0) {
            System.out.println(indent + this);
        } else {
            System.out.println(indent + "with right child: " + this);
        }
        if (left != null) {
            left.printTreeParams(depth + 2, -1);
        }
        if (right != null) {
            right.printTreeParams(depth + 2, +1);
        }
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