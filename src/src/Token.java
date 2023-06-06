package src;

public class Token {

    final TipoToken tipo;
    final String lexema;
    Object literal;
    int posicion;

    public Token(TipoToken tipo, String lexema, Object literal, int posicion) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.posicion = posicion;
    }

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof TipoToken)) {
            return false;
        }
        TipoToken c = (TipoToken) o;
        return this.tipo.equals(c);
    }

    public String toString() {
        return tipo + " " + lexema + " " + literal;
    }
}
