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
    symbolTable.forEach((key, value) -> System.out.println(key + " : " + value));
    System.out.println("Analisis semantico completado con exito");
    return true;
  }

  private void metotodoPR() {
    if (tokens.get(index).getValor().equals("int")) {
      index++;
      String i = tokens.get(index).getValor();
      index++;
      if (tokens.get(index).getValor().equals("=")) {
        index++;
        if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION) && isNumber(tokens.get(index).getValor())) {
          symbolTable.put(i, TokenType.NUMBER);
        } else if (tokens.get(index).getTipo().equals(TokenType.IDENTIFICADOR)) {
          if (symbolTable.containsKey(tokens.get(index).getValor())) {
            symbolTable.put(i, TokenType.NUMBER);
          } else {
            System.out.println("Error semantico: variable no declarada");
            throw new RuntimeException("Error semantico: variable No declarada");
          }
        }
      } else if (tokens.get(index).getTipo().equals(TokenType.PUNTOYCOMA)) {
        symbolTable.put(i, TokenType.NUMBER);
      }

    } else if (tokens.get(index).getValor().equals("string")) {
      index++;
      String i = tokens.get(index).getValor();
      index++;
      if (tokens.get(index).getValor().equals("=")) {
        index++;
        if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION)) {
          symbolTable.put(i, TokenType.STRING);
        } else if (tokens.get(index).getTipo().equals(TokenType.IDENTIFICADOR)) {
          if (symbolTable.containsKey(tokens.get(index).getValor())) {
            symbolTable.put(i, TokenType.STRING);
          } else {
            System.out.println("Error semantico: variable no declarada");
            throw new RuntimeException("Error semantico: variable No declarada");
          }
        }
      } else if (tokens.get(index).getTipo().equals(TokenType.PUNTOYCOMA)) {
        symbolTable.put(i, TokenType.STRING);
      }
    } else if (tokens.get(index).getValor().equals("float")) {
      index++;
      String i = tokens.get(index).getValor();
      index++;
      if (tokens.get(index).getValor().equals("=")) {
        index++;
        if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION)) {
          symbolTable.put(i, TokenType.FLOAT);
        } else if (tokens.get(index).getTipo().equals(TokenType.IDENTIFICADOR)) {
          if (symbolTable.containsKey(tokens.get(index).getValor())) {
            symbolTable.put(i, TokenType.FLOAT);
          } else {
            System.out.println("Error semantico: variable no declarada");
            throw new RuntimeException("Error semantico: variable No declarada");
          }
        }
      } else if (tokens.get(index).getTipo().equals(TokenType.PUNTOYCOMA)) {
        symbolTable.put(i, TokenType.FLOAT);
      }
    }
  }

  private boolean isNumber(String x) {
    if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION)) {
      if (x.matches("[0-9]+")) {
        return true;
      } else {
        System.out.println("Error semantico: valor no es un numero");
        throw new RuntimeException("Error semantico: valor no es un numero");
      }

    }
    return true;
  }
}
