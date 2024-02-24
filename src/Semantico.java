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
                case ASIGNACION:
                    // Perform semantic check for assignment
                    checkAssignment();
                    break;
                case OP:
                    // Perform semantic check for arithmetic operation
                    checkArithmeticOperation(token.getTipo());
                    break;
                // Add more cases as needed
                default:
                    throw new UnsupportedOperationException("Unsupported token type: " + token.getTipo());
            }
            index++;
        }
        return true;
    }

    private void checkAssignment() {
        // Assuming the format is IDENTIFIER ASIGNACION EXPRESSION
        Token identifier = tokens.get(index - 1);
        Token expression = tokens.get(index + 1);

        // Check if the variable has been declared
        if (!symbolTable.containsKey(identifier.getValor())) {
            throw new UnsupportedOperationException("Variable " + identifier.getValor() + " has not been declared");
        }

        // Check if the types are compatible
        if (symbolTable.get(identifier.getValor()) != expression.getTipo()) {
            throw new UnsupportedOperationException("Type mismatch: cannot assign " + expression.getTipo() + " to " + symbolTable.get(identifier.getValor()));
        }
    }

    private void checkArithmeticOperation(TokenType operationType) {
        // Assuming the format is EXPRESSION OPERATION EXPRESSION
        Token leftExpression = tokens.get(index - 1);
        Token rightExpression = tokens.get(index + 1);

        // Check if the types are compatible
        if (leftExpression.getTipo() != rightExpression.getTipo()) {
            throw new UnsupportedOperationException("Type mismatch: cannot perform " + operationType + " operation on " + leftExpression.getTipo() + " and " + rightExpression.getTipo());
        }
    }
}