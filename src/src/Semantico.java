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
      handleIntVariable();
    } else if (tokens.get(index).getValor().equals("string")) {
      handleStringVariable();
    } else if (tokens.get(index).getValor().equals("float")) {
      handleFloatVariable();
    } else if (tokens.get(index).getValor().equals("mostrar")) {
      handlePrint();
    } else if (tokens.get(index).getValor().equals("if")) {
      handleIf();
    }
  }

  private boolean handleCondicion() {

    return false;
  }

  private void handleIf() {
    System.out.println(tokens.get(index).getValor() + " : " + tokens.get(index).getTipo());
    index++;
    index++;
    if (isVariableIntOrNumber()) {
      index++;
      if (!isVariableIntOrNumber()) {
        throw new RuntimeException("Error semantico: variable No declarada o valor no es un numero");
      }
    }
  }

  private boolean isVariableIntOrNumber() {
    if (symbolTable.containsKey(tokens.get(index).getValor())) {
      return true;
    } else if (isNumber(tokens.get(index).getValor())) {
      return true;
    } else {
      return false;
    }
  }

  private boolean handlePrint() {
    System.out.println(tokens.get(index).getValor() + " : " + tokens.get(index).getTipo());
    index++;
    index++;
    if (symbolTable.containsKey(tokens.get(index).getValor())) {
      return true;
    } else {
      System.out.println("Error semantico: variable no declarada");
      System.out.println(tokens.get(index).getValor() + " : " + tokens.get(index).getTipo());
      throw new RuntimeException("Error semantico: variable No declarada");
    }
  }

  private void handleIntVariable() {
    // Skip the "int" token
    index++;
    String i = tokens.get(index).getValor();
    // Skip the variable name
    index++;
    if (tokens.get(index).getValor().equals("=")) {
      // Skip the "=" token
      index++;
      // Check if the next token is a number and if it is, add the variable to the
      // symbol table
      if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION)) {
        if (isNumber(tokens.get(index).getValor())) {
          symbolTable.put(i, TokenType.NUMBER);
          index++;
        } else {
          throw new RuntimeException("Error semantico: valor no es un numero");
        }
        // If the next token is an identifier, check if it's a variable declaration
      } else if (tokens.get(index).getTipo().equals(TokenType.IDENTIFICADOR)) {
        handleVariableAssignment(i);
      }
    } else if (tokens.get(index).getTipo().equals(TokenType.PUNTOYCOMA)) {
      symbolTable.put(i, TokenType.NUMBER);
      index++;
    } else {
      System.out.println("Error semantico: variable no declarada");
      throw new RuntimeException("Error semantico: variable No declarada");
    }
  }

  private void handleStringVariable() {
    index++;
    String i = tokens.get(index).getValor();
    index++;
    if (tokens.get(index).getValor().equals("=")) {
      index++;
      if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION)) {
        if (isString(tokens.get(index).getValor())) {
          symbolTable.put(i, TokenType.STRING);
          index++;
        } else {
          throw new RuntimeException("Error semantico: valor no es un String");
        }

      } else if (tokens.get(index).getTipo().equals(TokenType.IDENTIFICADOR)) {
        handleVariableAssignment(i);
      }
    } else if (tokens.get(index).getTipo().equals(TokenType.PUNTOYCOMA)) {
      symbolTable.put(i, TokenType.STRING);
      index++;
    }
  }

  private void handleFloatVariable() {
    index++;
    String i = tokens.get(index).getValor();
    index++;
    if (tokens.get(index).getValor().equals("=")) {
      index++;
      if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION) && isFloat(tokens.get(index).getValor())) {
        symbolTable.put(i, TokenType.FLOAT);
        index++;
      } else if (tokens.get(index).getTipo().equals(TokenType.IDENTIFICADOR)) {
        handleVariableAssignment(i);
      }
    } else if (tokens.get(index).getTipo().equals(TokenType.PUNTOYCOMA)) {
      symbolTable.put(i, TokenType.FLOAT);
      index++;
    }
  }

  private void handleVariableAssignment(String variableName) {
    if (symbolTable.containsKey(tokens.get(index).getValor())) {
      symbolTable.put(variableName, TokenType.NUMBER);
    } else {
      System.out.println("Error semantico: variable no declarada");
      throw new RuntimeException("Error semantico: variable No declarada");
    }
  }

  private boolean isNumber(String x) {
    if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION)) {
      if (x.matches("[0-9]+")) {
        return true;
      } else {
        System.out.println("Error semantico: valor no es un numero");
        return false;
      }
    }
    return true;
  }

  private boolean isFloat(String x) {
    if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION)) {
      if (x.matches("[0-9]+.[0-9]+")) {
        return true;
      } else {
        System.out.println("Error semantico: valor no es un numero flotante");
        throw new RuntimeException("Error semantico: valor no es un numero flotante");
      }
    }
    return true;
  }

  private boolean isString(String x) {
    if (tokens.get(index).getTipo().equals(TokenType.ASIGNACION)) {
      if (x.matches("\"[a-zA-Z]+\"")) {
        return true;
      } else {
        System.out.println("Error semantico: valor no es una cadena");
        return false;
      }
    }
    return true;
  }
}
