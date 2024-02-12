import java.util.ArrayList;

public class Scanner {
    
    public Token[] scanear(String source) {
        int posicion = 0;
        ArrayList<Token> tokens = new ArrayList<>();
        while (posicion < source.length()) {
            char c = source.charAt(posicion);

            if (c == 'i' && source.substring(posicion, posicion + 3).equals("ini")) {
                tokens.add(new Token(TokenType.INI, "ini"));
                posicion += 3;
            } else if (Character.isLetter(c)) {
                posicion = tokenizeIdentifier(source, posicion, tokens);
            } else if (c == ';') {
                tokens.add(new Token(TokenType.PUNTOYCOMA, ";"));
                posicion++;
            } else if (c == '{') {
                tokens.add(new Token(TokenType.LLAVEIZQ, "{"));
                posicion++;
            } else if (c == '}') {
                tokens.add(new Token(TokenType.LLAVEDER, "}"));
                posicion++;
            } else if (Character.isDigit(c)) {
                posicion = tokenizeNumber(source, posicion, tokens);
            } else if (c == '=') {
                tokens.add(new Token(TokenType.IGUAL, "="));
                posicion++;
            } else if (c == '+') {
                tokens.add(new Token(TokenType.MAS, "+"));
                posicion++;
            } else if (c == '-') {
                tokens.add(new Token(TokenType.MENOS, "-"));
                posicion++;
            } else if (c == '*') {
                tokens.add(new Token(TokenType.MULT, "*"));
                posicion++;
            } else if (c == '/') {
                tokens.add(new Token(TokenType.DIV, "/"));
                posicion++;
            } else if (c == '>') {
                tokens.add(new Token(TokenType.MAYORQUE, ">"));
                posicion++;
            } else if (c == '<') {
                tokens.add(new Token(TokenType.MENORQUE, "<"));
                posicion++;
            } else if (c == '(') {
                tokens.add(new Token(TokenType.PARI, "("));
                posicion++;
            } else if (c == ')') {
                tokens.add(new Token(TokenType.PARD, ")"));
                posicion++;
            } else if (Character.isWhitespace(c) || c == '\n' || c == '\r' || c == '\t') {
                posicion++;
            } else if (source.substring(posicion, posicion + 2).equals("if")) {
                tokens.add(new Token(TokenType.IF, "if"));
                posicion += 2;
            } else {
                throw new IllegalStateException("No se puede tokenizar el caracter: " + c);
            }
        }

        return tokens.toArray(new Token[tokens.size()]);
    }
    private int tokenizeIdentifier(String source, int position, ArrayList<Token> tokens) {
        int start = position;
        while (position < source.length() && (Character.isLetterOrDigit(source.charAt(position)) || source.charAt(position) == '_')) {
            position++;
        }
        tokens.add(new Token(TokenType.IDENTIFICADOR, source.substring(start, position)));
        return position;
    }

    private int tokenizeNumber(String source, int position, ArrayList<Token> tokens) {
        int start = position;
        while (position < source.length() && Character.isDigit(source.charAt(position))) {
            position++;
        }
        tokens.add(new Token(TokenType.NUMEROS, source.substring(start, position)));
        return position;
    }
}
