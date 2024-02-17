import java.util.*;

public class Parser {
    private List<Token> tokens;
    private int index;

    public Parser(Token[] tokens) {
        this.tokens = Arrays.asList(tokens);
        this.index = 0;
    }

    public void parse() {
        if (iniBlock()) {
            System.out.println("Programa correcto");
        } else {
            System.out.println("Programa incorrecto");
        }
    }

    private boolean iniBlock() {
        if (match(TokenType.PR) && match(TokenType.LLAVEIZQ)) {
            if (tokens.get(index).getTipo().equals(TokenType.LLAVEDER) && index == tokens.size() - 1) {
                return true;
            }
            if (statements()) {
                return true;
            }
            if (match(TokenType.LLAVEDER)) {
                return true;
            }
            
            return false;
        }else{
            System.out.println("Error: Expected PR LLAVEIZQ but found " + tokens.get(index).getValor() + " at token "+index);
            return false;
        }
       
    }

    private boolean statements() {
        boolean flag = true;
        while (index < tokens.size() && flag) {
            System.out.println("Statements " + tokens.get(index).getValor() + " at token "+index);
           flag = statement();
        }
        if (!flag) {
            System.out.println("Error: Expected statement but found " + tokens.get(index).getValor() + " at token "+index+ " fin de statements");
        }
        return flag;
    }

    private boolean statement() {
        System.out.println("Statement " + tokens.get(index).getValor() + " at token "+index);
        if (match(TokenType.PR) && tokens.get(index).getValor().equals("int")) {
            System.out.println("Variable declaration int "+tokens.get(index).getValor());
            variable_declarationInt();
            return true;
        } else if (match(TokenType.PR) && tokens.get(index).getValor().equals("string")) {
            System.out.println("Variable declaration String"+tokens.get(index).getValor());
            variable_declarationString();
            return true;
        }else if(match(TokenType.IDENTIFICADOR)){
            assignment();
            return true;
        }else if(match(TokenType.PR) && tokens.get(index).getValor().equals("if")){
            if_statement();
            return true;
        }else if(match(TokenType.PR) && tokens.get(index).getValor().equals("for")){
            for_statement();
            return true;
        }else if(match(TokenType.PR) && tokens.get(index).getValor().equals("while")){
            while_statement();
            return true;
        }else if(match(TokenType.PR) && tokens.get(index).getValor().equals("print")){
            printing();
            return true;
        }else{
            System.out.println("Error: Expected PR but found " + tokens.get(index).getValor() + " at token "+index + " fin de statement");
            return false;
        }
        
    }

    private void variable_declarationInt() {
        System.out.println("Variable declaration"+tokens.get(index).getValor() + " at token "+index);
        type();
        identifier();
        if (match(TokenType.OP)) {
            expression();
        }
        match(TokenType.PUNTOYCOMA);
    }

    private void variable_declarationString() {
        System.out.println("Variable declaration String"+tokens.get(index).getValor() + " at token "+index);
        type();
        identifier();
        if (match(TokenType.OP)) {
            string();
        }
        match(TokenType.PUNTOYCOMA);
    }

    private void assignment() {
        identifier();
        match(TokenType.OP);
        expression();
        match(TokenType.PUNTOYCOMA);
    }

    private void if_statement() {
        match(TokenType.PARI);
        condition();
        match(TokenType.PARD);
        match(TokenType.LLAVEIZQ);
        statements();
        match(TokenType.LLAVEDER);
        if (match(TokenType.PR)) {
            match(TokenType.LLAVEIZQ);
            statements();
            match(TokenType.LLAVEDER);
        }
    }

    private void for_statement() {
        match(TokenType.PARI);
        variable_declarationInt();
        match(TokenType.PUNTOYCOMA);
        condition();
        match(TokenType.PUNTOYCOMA);
        assignment();
        match(TokenType.PARD);
        match(TokenType.LLAVEIZQ);
        statements();
        match(TokenType.LLAVEDER);
    }

    private void while_statement() {
        match(TokenType.PARI);
        condition();
        match(TokenType.PARD);
        match(TokenType.LLAVEIZQ);
        statements();
        match(TokenType.LLAVEDER);
    }

    private void printing() {
        match(TokenType.PARI);
        identifier();
        match(TokenType.PARD);
        match(TokenType.PUNTOYCOMA);
    }

    private void type() {
        match(TokenType.PR);
    }

    private void identifier() {
        match(TokenType.IDENTIFICADOR);
    }

    private void expression() {
        factor();
        if (match(TokenType.OP)) {
            expression();
        }
    }

    private void string() {
        match(TokenType.STRING);
    }

    private void condition() {
        expression();
        comparison_operator();
        expression();
    }

    private void factor() {
        if (match(TokenType.PARI)) {
            expression();
            match(TokenType.PARD);
        } else {
            identifier();
        }
    }

    private void comparison_operator() {
        match(TokenType.CO);
        // Add other comparison operators here
    }

    private boolean match(TokenType expectedToken) {
        System.out.println("Matching " + expectedToken +" valor token:"+ tokens.get(index).getValor() + " tipo token:" + tokens.get(index).getTipo() + " at token "+index);
        if (index < tokens.size() && tokens.get(index).getTipo() == expectedToken) {
            index++;
            System.out.println("Matched " + expectedToken + " at line "+tokens.get(index));
            return true;
        }
        System.out.println("Error: Expected " + expectedToken + " but found " + tokens.get(index).getValor());
        //throw new RuntimeException("Syntax error" + "Error: Expected " + expectedToken + " but found " + tokens.get(index).getValor()+" at token "+index);
        return false;
    }
}
