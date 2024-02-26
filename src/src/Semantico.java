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
                case IDENTIFICADOR:
                
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
                case PR:
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
}