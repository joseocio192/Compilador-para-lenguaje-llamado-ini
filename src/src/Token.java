package src;
public class Token {
    private final TokenType tipo;
    private final String valor;

    public Token(TokenType tipo, String valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public TokenType getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public String toString() {
        return String.format("(%s, %s)", tipo, valor);
    }  

}
