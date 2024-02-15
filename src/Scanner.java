import java.util.ArrayList;

public class Scanner{

    public Token[] scanear(String source) {
        int posicion = 0;
        ArrayList<Token> tokens = new ArrayList<>();
        while (posicion < source.length()) {
            char c = source.charAt(posicion);
            if (Character.isWhitespace(c) || c == '\n' || c == '\r' || c == '\t') {
                posicion++;
            }else{
                posicion = tokenizeCharacter(c, source, posicion, tokens);
            }
        }
        return tokens.toArray(new Token[tokens.size()]);
    }

    private int tokenizeCharacter(char c, String source, int posicion, ArrayList<Token> tokens) {
        if (isIniKeyword(c, source, posicion)) {
            return tokenizeIni(posicion, tokens);
        } else if (isIfKeyword(c, source, posicion)) {
            return tokenizeIf(posicion, tokens);
        } else if (isTokenString(c, source, posicion)) {
            return tokenizeString(posicion, tokens);
        }else if (isElseKeyword(c, source, posicion)) {
            return tokenizeElse(posicion, tokens);
        } else if (isMostrarKeyword(c, source, posicion)) {
            return tokenizeMostrar(posicion, tokens);
        } else if (isIntKeyword(c, source, posicion)) {
            return tokenizeInt(posicion, tokens);
        } else if (c == '"') {
            return tokenizeStringValue(posicion,source, tokens);
        }else if (Character.isLetter(c)) {
            return tokenizeIdentifier(source, posicion, tokens);
        } else if (Character.isDigit(c)) {
            return tokenizeNumber(source, posicion, tokens);
        } else {
            return tokenizeSymbol(c, source, posicion, tokens);
        }
    }

    private int tokenizeStringValue(int posicion, String source, ArrayList<Token> tokens) {
        int start = posicion;
        posicion++;
        while (posicion < source.length() && source.charAt(posicion) != '"') {
            posicion++;
        }
        if (posicion == source.length()) {
            throw new UnsupportedOperationException("Cadena no cerrada: " + source.substring(start, posicion));
        }
        tokens.add(new Token(TokenType.STRING, source.substring(start, posicion+1)));
        return posicion + 1;
    }

    private int tokenizeString(int posicion, ArrayList<Token> tokens) {
        tokens.add(new Token(TokenType.PR, "string"));
        return posicion + 6;
    }

    private boolean isTokenString(char c, String source, int posicion) {
        return source.substring(posicion, Math.min(posicion + 6, source.length())).equals("string") && (Character.isWhitespace(source.charAt(posicion + 6)) || source.charAt(posicion + 6) == '(');
    }

    private boolean isIniKeyword(char c, String source, int posicion) {
        return source.substring(posicion, Math.min(posicion + 3, source.length())).equals("ini") && (Character.isWhitespace(source.charAt(posicion + 3)) || source.charAt(posicion + 3) == '{');
    }

    private boolean isIfKeyword(char c, String source, int posicion) {
        return source.substring(posicion, Math.min(posicion + 2, source.length())).equals("if") && (Character.isWhitespace(source.charAt(posicion + 2)) || source.charAt(posicion + 2) == '(');
    }

    private boolean isElseKeyword(char c, String source, int posicion) {
        return source.substring(posicion, Math.min(posicion + 4, source.length())).equals("else") && (Character.isWhitespace(source.charAt(posicion + 4)) || source.charAt(posicion + 4) == '{');
    }

    private boolean isMostrarKeyword(char c, String source, int posicion) {
        return source.substring(posicion, Math.min(posicion + 7, source.length())).equals("mostrar") && (Character.isWhitespace(source.charAt(posicion + 7)) || source.charAt(posicion + 7) == '(');
    }

    private boolean isIntKeyword(char c, String source, int posicion) {
        return source.substring(posicion, Math.min(posicion + 3, source.length())).equals("int");
    }

    private int tokenizeInt(int posicion, ArrayList<Token> tokens) {
        tokens.add(new Token(TokenType.PR, "int"));
        return posicion + 3;
    }

    private int tokenizeMostrar(int posicion, ArrayList<Token> tokens) {
        tokens.add(new Token(TokenType.PR, "mostrar"));
        return posicion + 7;
    }

    private int tokenizeElse(int posicion, ArrayList<Token> tokens) {
        tokens.add(new Token(TokenType.PR, "else"));
        return posicion + 4;
    }

    private int tokenizeIf(int posicion, ArrayList<Token> tokens) {
        tokens.add(new Token(TokenType.PR, "if"));
        return posicion + 2;
    }

    private int tokenizeIni(int posicion, ArrayList<Token> tokens) {
        //identificar si ini no esta mal escrito, y si si tirar error
        tokens.add(new Token(TokenType.PR, "ini"));
        return posicion + 3;
    }

    private int tokenizeSymbol(char c, String source, int posicion, ArrayList<Token> tokens) {
        TokenType type = null;
        switch (c) {
            case ';':
                type = TokenType.PUNTOYCOMA;
                break;
            case '(':
                type = TokenType.PARI;
                break;
            case ')':
                type = TokenType.PARD;
                break;
            case '{':
                type = TokenType.LLAVEIZQ;
                break;
            case '}':
                type = TokenType.LLAVEDER;
                break;
            case '=':
                if (source.charAt(posicion + 1) == '>'){
                    tokens.add(new Token(TokenType.CO, "=>"));
                    return posicion + 2;
                }
                if (source.charAt(posicion + 1) == '='){
                    tokens.add(new Token(TokenType.CO, "=="));
                    return posicion + 1;
                } else {
                    type = TokenType.OP;
                }
                break;
            case '+':
                type = TokenType.OP;
                break;
            case '-':
                type = TokenType.OP;
                break;
            case '*':
                type = TokenType.OP;
                break;
            case '/':
                type = TokenType.OP;
                break;
            case '>':
                type = TokenType.CO;
                break;
            case '<':
                if (source.charAt(posicion + 1) == '='){
                    tokens.add(new Token(TokenType.CO, "<="));
                    return posicion + 2;
                }
                type = TokenType.CO;
                break;
            case '!':
                if (source.charAt(posicion + 1) == '='){
                    tokens.add(new Token(TokenType.CO, "!="));
                    return posicion + 1;
                }
                break; 
        }
        if (type != null) {
            tokens.add(new Token(type, String.valueOf(c)));
            posicion++;
        }
        if (type == null) {
            throw new UnsupportedOperationException("Caracter no soportado: " + c);
        }
        return posicion;
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
        //identificar que el numero sea numero y no un identificador erroneo
        if (position < source.length() && Character.isLetter(source.charAt(position))) {
            throw new UnsupportedOperationException("Identificador no valido: " + source.substring(start, position + 1));
        }
        tokens.add(new Token(TokenType.NUMBER, source.substring(start, position)));
        return position;
    }
}