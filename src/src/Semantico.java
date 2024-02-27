package src;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Semantico {
    private List<Token> tokens;
    private int index;
    private Map<String, TokenType> symbolTable; // Symbol table to keep track of variable declarations

    public Semantico(Token[] tokens) {
        this.tokens = Arrays.asList(tokens);
        this.index = 0;
        this.symbolTable = new HashMap<>();
    }

    public boolean isSemanticallyCorrect() {
        // Start semantic analysis
        while (index < tokens.size()) {
            Token token = tokens.get(index);
            // Check the token type and perform the appropriate semantic check
            switch (token.getTipo()) {
                case PR:
                metotodoPR();
                    break;
                case ASIGNACION:
                    break;
                case CO:
                    break;
                case FLOAT:
                    break;
                case LLAVEDER:
                    break;
                case LLAVEIZQ:
                    break;
                case NUMBER:
                    break;
                case OP:
                    break;
                case PARD:
                    break;
                case PARI:
                    break;
                case PUNTOYCOMA:
                    break;
                case STRING:
                    break;
                default:
                    break;
            }
            index++;
        }
        return true;
    }

    private void metotodoPR() {
        if (tokens.get(index).getValor().equals("int")) {
            index++;
            String i =tokens.get(index).getValor();
            index++;
            if(tokens.get(index).getTipo().equals(TokenType.ASIGNACION)){
                index++;
                if(tokens.get(index).getTipo().equals(TokenType.NUMBER)){
                    symbolTable.put(i, TokenType.IDENTIFICADOR);
                }
                else if(tokens.get(index).getTipo().equals(TokenType.IDENTIFICADOR)){
                if (symbolTable.containsKey(tokens.get(index).getValor())) {
                    symbolTable.put(i, symbolTable.get(tokens.get(index).getValor()));
                }
                else{
                    System.out.println("Error semantico: variable no declarada");
                    throw new RuntimeException("Error semantico: variable no declarada");
                }
                }
            }
        }
    }
}