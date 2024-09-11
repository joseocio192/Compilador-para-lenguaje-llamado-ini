package src;

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
      throw new RuntimeException("Syntax error at token " + index);
    }
  }

  private boolean iniBlock() {
    if (tokens.get(index).getValor().equals("ini") && match(TokenType.PR) && match(TokenType.LLAVEIZQ)) {
      if (!statements()) {
        return false;
      }
      return (tokens.get(index).getTipo().equals(TokenType.LLAVEDER) && index == tokens.size() - 1);
    } else {
      return false;
    }
  }

  private boolean statements() {
    boolean flag = true;
    while (index < (tokens.size() - 1)) {
      // System.out.println("Statement at token "+index+" of "+tokens.size());
      if (tokens.get(index).getTipo() == TokenType.LLAVEDER) {
        break;
      }
      flag = statement();
      System.out.println(flag + " at token " + index + " of " + tokens.size());
      if (!flag) {
        break;
      }
    }

    return flag;
  }

  private boolean statement() {

    int tempIndex = index;
    if (variable_declarationInt()) {
      System.out.println("variable_declarationInt");
      return true;
    } else {
      index = tempIndex;
    }
    if (variable_declarationString()) {
      System.out.println("variable_declarationString");
      return true;
    } else {
      index = tempIndex;
    }
    if (assignment()) {
      System.out.println("assignment");
      return true;
    } else {
      index = tempIndex;
    }
    if (if_statement()) {
      System.out.println("if_statement true");
      return true;
    } else {
      index = tempIndex;
    }
    System.out.println("for_statement");
    if (for_statement()) {
      System.out.println("for_statement true");
      return true;
    } else {
      index = tempIndex;
    }
    if (while_statement()) {
      System.out.println("while_statement true");
      return true;
    } else {
      index = tempIndex;
    }
    if (printing()) {
      System.out.println("printing");
      return true;
    } else {
      index = tempIndex;
    }

    if (match(TokenType.LLAVEDER)) {
      index--;
      System.out.println("LLAVEDER");
      return true;
    }
    return false;
  }

  private boolean variable_declarationInt() {
    if (!type()) {
      return false;
    }
    if (!identifier()) {
      return false;
    }

    if (match(TokenType.PUNTOYCOMA)) {
      return true;
    }

    if (!match(TokenType.OP)) {
      return false;
    }
    if (!expression()) {
      return false;
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

  private boolean assignment() {
    if (!identifier()) {
      return false;
    }
    if (!match(TokenType.OP)) {
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
    if (tokens.get(index).getValor().equals("else")) {
      index++;
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
    if (match(TokenType.LLAVEDER)) {
      return true;
    }
    if (!statements()) {
      return false;
    }
    return match(TokenType.LLAVEDER);
  }

  private boolean while_statement() {
    if (!match(TokenType.PR) && tokens.get(index - 1).getValor().equals("while")) {
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
    return match(TokenType.LLAVEDER);
  }

  private boolean printing() {
    if (!match(TokenType.PR) && tokens.get(index - 1).getValor().equals("mostrar")) {
      return false;
    }
    if (!match(TokenType.PARI)) {
      return false;
    }
    if (!identifier()) {
      return false;
    }
    if (!match(TokenType.PARD)) {
      return false;
    }
    return match(TokenType.PUNTOYCOMA);
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
    int tempIndex = index;
    if (match(TokenType.OP)) {
      if (!factor()) {
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
    if (!factor()) {
      return false;
    }
    if (!comparison_operator()) {
      return false;
    }
    return factor();
  }

  private boolean factor() {
    if (match(TokenType.IDENTIFICADOR)) {
      return true;
    }
    if (match(TokenType.ASIGNACION)) {
      return true;
    }
    if (match(TokenType.NUMBER)) {
      return true;
    }
    if (match(TokenType.FLOAT)) {
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

    if (index < tokens.size()) {
      // System.out.println("Matching " + expectedToken +" valor token:"+
      // tokens.get(index).getValor() + " tipo token:" + tokens.get(index).getTipo() +
      // " at token "+index);
      if (tokens.get(index).getTipo() == expectedToken) {
        System.out.println("Matched " + expectedToken + " at line " + tokens.get(index));
        index++;
        return true;
      }
      // System.out.println("Error: Expected " + expectedToken + " but found " +
      // tokens.get(index).getValor()+":"+tokens.get(index).getTipo());
      return false;
    }

    // throw new RuntimeException("Syntax error" + "Error: Expected " +
    // expectedToken + " but found " + tokens.get(index).getValor()+" at token
    // "+index);
    return false;
  }
}
