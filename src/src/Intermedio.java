package src;

import java.util.*;

public class Intermedio {
  public String codigoIntermedio;

  private List<Token> tokens;
  private int index;
  private Map<String, TokenType> symbolTable;
  private Map<String, String> symbolTableValor;
  private String codigoEnsamblador;
  private int etiqueta = 0;

  public Intermedio(Token[] tokens) {
    this.tokens = Arrays.asList(tokens);
    this.index = 0;
    this.symbolTable = Semantico.getSymbolTable();
    this.symbolTableValor = Semantico.getSymbolTableValor();
    this.codigoEnsamblador = "";
  }

  public String getIntermedioCode() {
    StringBuilder asemblerCode = new StringBuilder(".MODEL SMALL\n.STACK 100H\n.DATA\n");
    for (Map.Entry<String, TokenType> entry : symbolTable.entrySet()) {
      System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
      String key = entry.getKey();
      TokenType type = entry.getValue();
      if (symbolTableValor.containsKey(key)) {
        if (type == TokenType.NUMBER) {
          asemblerCode.append(key).append(" dd ").append(symbolTableValor.get(key)).append("\n");
        } else if (type == TokenType.STRING) {
          asemblerCode.append(key).append(" db ").append(symbolTableValor.get(key)).append(", 0\"\n");
        }
      }
    }
    asemblerCode.append(".CODE\nMAIN PROC\r\nMOV AX,@data\r\nMOV DS,AX\n");
    codigoEnsamblador = asemblerCode.toString();
    iniBlock();
    codigoEnsamblador += "MOV AH,4CH\r\nINT 21H\r\nMAIN ENDP\r\nEND MAIN";
    System.out.println(codigoEnsamblador);
    return codigoEnsamblador;
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
    int tempIndex2 = index;
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
      tempIndex2 = index;
      index = tempIndex;
      traducirAsignacion();
      index = tempIndex2;
      return true;
    } else {
      index = tempIndex;
    }
    if (if_statement()) {
      System.out.println("if_statement true");
      tempIndex2 = index;
      index = tempIndex;
      traducirifstatement();
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

  private void traducirifstatement() {
    etiqueta++;
    if (tokens.get(index).getValor().equals("if")) {
      index++;
    }
    if (!match(TokenType.PARI)) {
      return;
    }
    if (tokens.get(index).getTipo() == TokenType.IDENTIFICADOR) {
      String identificador = tokens.get(index).getValor();
      codigoEnsamblador += "MOV eax, " + identificador + "\n";
      index++;
    } else {
      codigoEnsamblador += "MOV eax, " + tokens.get(index).getValor() + "\n";
      index++;
    }
    int tempEtiqueta = etiqueta;
    switch (tokens.get(index).getValor()) {
      case "<=":
      codigoEnsamblador += "CMP eax, " + tokens.get(index + 1).getValor() + "\n";
      codigoEnsamblador += "JLE etiqueta" + etiqueta + "\n";
      break;
      case ">=":
      codigoEnsamblador += "CMP eax, " + tokens.get(index + 1).getValor() + "\n";
      codigoEnsamblador += "JGE etiqueta" + etiqueta + "\n";
      break;
      case "<":
      codigoEnsamblador += "CMP eax, " + tokens.get(index + 1).getValor() + "\n";
      codigoEnsamblador += "JL etiqueta" + etiqueta + "\n";
      break;
      case ">":
      codigoEnsamblador += "CMP eax, " + tokens.get(index + 1).getValor() + "\n";
      codigoEnsamblador += "JG etiqueta" + etiqueta + "\n";
      break;
      case "==":
      codigoEnsamblador += "CMP eax, " + tokens.get(index + 1).getValor() + "\n";
      codigoEnsamblador += "JE etiqueta" + etiqueta + "\n";
      break;
      case "!=":
      codigoEnsamblador += "CMP eax, " + tokens.get(index + 1).getValor() + "\n";
      codigoEnsamblador += "JNE etiqueta" + etiqueta + "\n";
      break;
      default:
      break;
    }
    index++;
    index++;
    index++;
    if (!match(TokenType.LLAVEIZQ)) {
      return;
    }
    if (match(TokenType.LLAVEDER)) {
      codigoEnsamblador += "etiqueta" + tempEtiqueta + ":\n";
      return;
    }
    while (index < tokens.size() - 1) {
      if (tokens.get(index).getTipo() == TokenType.LLAVEDER) {
        break;
      }
      statement();
    }
    if (!match(TokenType.LLAVEDER)) {
      return;
    }
    codigoEnsamblador += "etiqueta" + tempEtiqueta + ":\n";
  }

  private void traducirAsignacion() {
    String identificador = tokens.get(index).getValor();
    index++;
    if (tokens.get(index).getValor().equals("+")) {
      index++;
      codigoEnsamblador += "MOV eax, " + identificador + "\n";
      if (tokens.get(index).getTipo() == TokenType.NUMBER) {
        codigoEnsamblador += "add eax, " + tokens.get(index).getValor() + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      } else {
        codigoEnsamblador += "add eax, " + symbolTableValor.get(tokens.get(index).getValor()) + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      }
    } else if (tokens.get(index).getValor().equals("-")) {
      index++;
      codigoEnsamblador += "MOV eax, " + identificador + "\n";
      if (tokens.get(index).getTipo() == TokenType.NUMBER) {
        codigoEnsamblador += "sub eax, " + tokens.get(index).getValor() + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      } else {
        codigoEnsamblador += "sub eax, " + symbolTableValor.get(tokens.get(index).getValor()) + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      }
    } else if (tokens.get(index).getValor().equals("*")) {
      index++;
      codigoEnsamblador += "MOV eax, " + identificador + "\n";
      if (tokens.get(index).getTipo() == TokenType.NUMBER) {
        codigoEnsamblador += "mul eax, " + tokens.get(index).getValor() + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      } else {
        codigoEnsamblador += "mul eax, " + symbolTableValor.get(tokens.get(index).getValor()) + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      }
    } else if (tokens.get(index).getValor().equals("/")) {
      index++;
      codigoEnsamblador += "MOV eax, " + identificador + "\n";
      if (tokens.get(index).getTipo() == TokenType.NUMBER) {
        codigoEnsamblador += "div eax, " + tokens.get(index).getValor() + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      } else {
        codigoEnsamblador += "div eax, " + symbolTableValor.get(tokens.get(index).getValor()) + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      }
    } else if (tokens.get(index).getValor().equals("^")) {
      index++;
      codigoEnsamblador += "MOV eax, " + identificador + "\n";
      if (tokens.get(index).getTipo() == TokenType.NUMBER) {
        codigoEnsamblador += "pow eax, " + tokens.get(index).getValor() + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      } else {
        codigoEnsamblador += "pow eax, " + symbolTableValor.get(tokens.get(index).getValor()) + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      }
    } else if (tokens.get(index).getValor().equals("=")) {
      index++;
      if (tokens.get(index).getTipo() == TokenType.NUMBER) {
        codigoEnsamblador += "MOV eax, " + tokens.get(index).getValor() + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      } else if (tokens.get(index).getTipo() == TokenType.ASIGNACION) {
        codigoEnsamblador += "MOV eax, " + tokens.get(index).getValor() + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      }else  {
        codigoEnsamblador += "MOV eax, " + symbolTableValor.get(tokens.get(index).getValor()) + "\n";
        codigoEnsamblador += "MOV " + identificador + ", eax\n";
      }
    }
  }

  private boolean variable_declarationInt() {
    System.out.println("variable_declarationInt");
    if (tokens.get(index).getTipo() != TokenType.PR || !tokens.get(index).getValor().equals("int")) {
      return false;
    }
    index++;

    if (!identifier() && !symbolTable.containsKey(tokens.get(index - 1).getValor())) {
      return false;
    }

    if (match(TokenType.PUNTOYCOMA)) {
      symbolTable.put(tokens.get(index - 2).getValor(), TokenType.NUMBER);
      symbolTableValor.put(tokens.get(index - 2).getValor(), "0");
      return true;
    }

    if (!match(TokenType.OP)) {
      return false;
    }
    if (match(TokenType.ASIGNACION)) {
      System.out.println(tokens.get(index - 3).getValor() + " " + tokens.get(index - 1).getValor());
      if (isAsignacionInt(tokens.get(index - 1).getValor())) {
        symbolTable.put(tokens.get(index - 3).getValor(), TokenType.NUMBER);
        symbolTableValor.put(tokens.get(index - 3).getValor(), tokens.get(index - 1).getValor());
      } else {
        throw new RuntimeException("Error: Expected NUMBER but found STRING at " + index);
      }
    }
    // print symbol table
    for (Map.Entry<String, TokenType> entry : symbolTable.entrySet()) {
      System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
    }
    System.out.println("true");
    return match(TokenType.PUNTOYCOMA);
  }

  private boolean variable_declarationString() {
    if (!type() && !tokens.get(index).getValor().equals("string")) {
      return false;
    }
    if (!identifier()) {
      return false;
    }
    if (match(TokenType.PUNTOYCOMA)) {
      symbolTable.put(tokens.get(index - 2).getValor(), TokenType.STRING);
      symbolTableValor.put(tokens.get(index - 2).getValor(), "0");
      return true;
    }
    if (!match(TokenType.OP)) {
      return false;
    }
    if (match(TokenType.ASIGNACION)) {
      if (isAsignacionString(tokens.get(index - 1).getValor())) {
        symbolTable.put(tokens.get(index - 3).getValor(), TokenType.STRING);
        symbolTableValor.put(tokens.get(index - 3).getValor(), tokens.get(index - 1).getValor());
      } else {
        throw new RuntimeException("Error: Expected STRING but found NUMBER at " + index);
      }
    }
    return match(TokenType.PUNTOYCOMA);
  }

  private boolean assignment() {
    TokenType temp = symbolTable.get(tokens.get(index).getValor());
    if (temp == null) {
      return false;
    }
    if (!identifier()) {
      return false;
    }
    if (!match(TokenType.OP)) {
      return false;
    }
    if (!expression(temp)) {
      return false;
    }
    return match(TokenType.PUNTOYCOMA);
  }

  private boolean if_statement() {
    if (match(TokenType.PR) && tokens.get(index - 1).getValor().equals("if")) {
      return true;
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

  private boolean expression(TokenType tipo) {
    TokenType temp = null;
    if (!factor()) {
      return false;
    } else {
      if (tokens.get(index - 1).getTipo() == TokenType.ASIGNACION) {
        if (isAsignacionString(tokens.get(index - 1).getValor())) {
          temp = TokenType.STRING;
        } else {
          temp = TokenType.NUMBER;
        }
      }
    }
    int tempIndex = index;
    if (match(TokenType.OP)) {
      if (!factor()) {
        index = tempIndex;
        return false;
      }
      temp = tokens.get(index).getTipo();
      return tipo == temp;
    }
    return tipo == temp;
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

  public boolean isAsignacionInt(String temp) {
    char x = temp.charAt(0);
    return x == '"' ? false : true;
  }

  private boolean isAsignacionString(String temp) {
    char x = temp.charAt(0);
    return x == '"' ? true : false;
  }
}
