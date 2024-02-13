import java.util.ArrayList;

public class ScannerNewIdea{
    
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
        if (c == 'i' && source.substring(posicion, Math.min(posicion + 3, source.length())).equals("ini")&& (Character.isWhitespace(source.charAt(posicion + 4))||source.charAt(posicion+4)=='{' )) {
            return tokenizeIni(posicion, tokens);
        } else if (source.substring(posicion, Math.min(posicion + 2, source.length())).equals("if") && (Character.isWhitespace(source.charAt(posicion + 2)) || source.charAt(posicion + 2) == '(')) {
            tokens.add(new Token(TokenType.IF, "if"));
            return posicion + 2;
        } else if (source.substring(posicion, Math.min(posicion + 4, source.length())).equals("else") && (Character.isWhitespace(source.charAt(posicion + 4)) || source.charAt(posicion + 4) == '{')) {
            tokens.add(new Token(TokenType.ELSE, "else"));
            return posicion + 4;
        }else if (Character.isLetter(c)) {
            return tokenizeIdentifier(source, posicion, tokens);
        } else if (Character.isDigit(c)) {
            return tokenizeNumber(source, posicion, tokens);
        } else {
            return tokenizeSymbol(c, source, posicion, tokens);
        }
    }

    private int tokenizeIni(int posicion, ArrayList<Token> tokens) {
        //identificar si ini no esta mal escrito, y si si tirar error
        
        tokens.add(new Token(TokenType.INI, "ini"));
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
                type = TokenType.IGUAL;
                break;
            case '+':
                type = TokenType.MAS;
                break;
            case '-':
                type = TokenType.MENOS;
                break;
            case '*':
                type = TokenType.MULT;
                break;
            case '/':
                type = TokenType.DIV;
                break;
            case '>':
                type = TokenType.MAYORQUE;
                break;
            case '<':
                type = TokenType.MENORQUE;
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
        tokens.add(new Token(TokenType.NUMEROS, source.substring(start, position)));
        return position;
    }
}