import java.util.*;

public class Parser {
    private List<Token> tokens;
    private int index;

    public Parser(Token[] tokens) {
        this.tokens = Arrays.asList(tokens);
        this.index = 0;
    }
    
    public boolean parse() {
        if (iniBlock()) {
            System.out.println("Programa correcto");
            return true;
        } else {
            System.out.println("Programa incorrecto");
            return false;
        }
    }

    private boolean iniBlock() {
        if (tokens.get(index).getValor().equals("ini") && match(TokenType.PR) && match(TokenType.LLAVEIZQ)) {
            if (!statements()) {
                return false;
            }
            return tokens.get(index).getTipo().equals(TokenType.LLAVEDER);
        } else {
            return false;
        }
    }

    private boolean statements() {
        boolean flag = true;
        while (index < (tokens.size()-1)) {
            System.out.println("Statement at token "+index+" of "+tokens.size());
            flag = statement();
            System.out.println(flag + " at token "+index+" of "+tokens.size());
            if (!flag) {
                break;
            }
        }
        return flag;
    }

    private boolean statement() {
        if (match(TokenType.LLAVEDER)) {
            index--;
            System.out.println("LLAVEDER");
            return true;
        }

        int tempIndex = index;
        if (variable_declarationInt()) {
            System.out.println("variable_declarationInt");
            return true;
        }else{
            index = tempIndex;
        }
        if (variable_declarationString()) {
            System.out.println("variable_declarationString");
            return true;
        }else{
            index = tempIndex;
        }
        if (assignment()) {
            System.out.println("assignment");
            return true;
        }else{
            index = tempIndex;
        }
        System.out.println("if statemant condition");
        if (if_statement()) {
            System.out.println("if_statement");
            return true;
        }else{
            index = tempIndex;
        }
        if (for_statement()) {
            System.out.println("for_statement");
            return true;
        }else{
            index = tempIndex;
        }
        if (while_statement()) {
            System.out.println("while_statement");
            return true;
        }else{
            index = tempIndex;
        }

        if (match(TokenType.LLAVEDER)) {
            index--;
            return true;
        }else{
            return false;
        }
    }

    private boolean variable_declarationInt() {
        if (!type()) {
            return false;
        }
        if (!identifier()) {
            return false;
        }

        if (match(TokenType.OP)) {
            return expression();
        }
        return match(TokenType.PUNTOYCOMA);
    }

    private boolean variable_declarationString() {
        if (!type()) {
            return false;
            
        }
        if (!identifier()) {
            return false;
        }
        if (match(TokenType.OP) && (!string())) {
                return match(TokenType.PUNTOYCOMA);
            
        }
        return true;
    }

    private boolean assignment(){
        if (!identifier()) {
            return false;
        }
        if (!match(TokenType.ASIGNACION)) {
            return false;
        }
        if (!expression()) {
            return false;
        }
        return match(TokenType.PUNTOYCOMA);

    }

    private boolean if_statement() {
        if (!match(TokenType.PR)) {
            return false;
        }
        if (!match(TokenType.PARI)) {
            return false;
        }
        if (!condition()) {
            return false;
        }
        if (!match(TokenType.PARD)) {
            return false;
        }
        if (!match(TokenType.LLAVEIZQ)) {
            return false;
        }
        if (match(TokenType.LLAVEDER)) {
            return true;
        }
        if (!statements()) {
            return false;
        }
        if (!match(TokenType.LLAVEDER)) {
            return false;
        }
        if (match(TokenType.PR) && (tokens.get(index).getValor().equals("else"))) {
                if (!match(TokenType.LLAVEIZQ)) {
                    return false;
                }
                if (!statements()) {
                    return false;
                }
                if (!match(TokenType.LLAVEDER)) {
                    return false;
                }
        }
        return true;
    }

    private boolean for_statement() {
        if (!match(TokenType.PR)) {
            return false;
        }
        if (!match(TokenType.PARI)) {
            return false;
        }
        if (!variable_declarationInt()) {
            return false;
        }
        if (!match(TokenType.PUNTOYCOMA)) {
            return false;
        }
        if (!condition()) {
            return false;
        }
        if (!match(TokenType.PUNTOYCOMA)) {
            return false;
        }
        if (!assignment()) {
            return false;
        }
        if (!match(TokenType.PARD)) {
            return false;
        }
        if (!match(TokenType.LLAVEIZQ)) {
            return false;
        }
        if (!statements()) {
            return false;
        }
        return match(TokenType.LLAVEDER);
    }

    private boolean while_statement() {
        if (!match(TokenType.PR)) {
            return false;
        }
        if (!match(TokenType.PARI)) {
            return false;
        }
        if (!condition()) {
            return false;
        }
        if (!match(TokenType.PARD)) {
            return false;
        }
        if (!match(TokenType.LLAVEIZQ)) {
            return false;
        }
        if (!statements()) {
            return false;
        }
        return match(TokenType.LLAVEDER);
    }

    private boolean printing() {
        match(TokenType.PARI);
        identifier();
        match(TokenType.PARD);
        match(TokenType.PUNTOYCOMA);
        return true;
    }

    private boolean type() {
        return match(TokenType.PR);
    }

    private boolean identifier() {
        return match(TokenType.IDENTIFICADOR);
    }

    private boolean expression() {
        if (!factor()) {
            return false;
        }
        if (match(TokenType.PUNTOYCOMA)) {
            return true;
        }
        int tempIndex = index;
        if (match(TokenType.OP)) {
            if (!expression()) {
                index = tempIndex;
                return false;
            }
            return true;
        }
        return true;

    }

    private boolean string() {
        return match(TokenType.STRING);
    }

    private boolean condition() {
        if (!expression()) {
            return false;
        }
        if (!comparison_operator()) {
            return false;
        }
        return expression();
    }

    private boolean factor() {
        if (match(TokenType.ASIGNACION)) {
            return true;
        }
        if (match(TokenType.IDENTIFICADOR)) {
            return true;
        }
        if (match(TokenType.NUMBER)) {
            return true;
        }
        if (match(TokenType.PARI)) {
            if (!expression()) {
                return false;
            }
            return match(TokenType.PARD);
        }
        return false;
    }

    private boolean comparison_operator() {
        return match(TokenType.CO);
        // Add other comparison operators here
    }

    private boolean match(TokenType expectedToken) {
        
        if (index < tokens.size() ) {
            System.out.println("Matching " + expectedToken +" valor token:"+ tokens.get(index).getValor() + " tipo token:" + tokens.get(index).getTipo() + " at token "+index);
            if (tokens.get(index).getTipo() == expectedToken) {
                System.out.println("Matched " + expectedToken + " at line "+tokens.get(index));
                index++;
                return true;
            }
           return false;
        }
        System.out.println("Error: Expected " + expectedToken + " but found " + tokens.get(index).getValor());
        //throw new RuntimeException("Syntax error" + "Error: Expected " + expectedToken + " but found " + tokens.get(index).getValor()+" at token "+index);
        return false;
    }
}
