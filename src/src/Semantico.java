package src;

import java.util.*;

public class Semantico {
  private List<Token> tokens;
  private int index;
  static private Map<String, TokenType> symbolTable;
  static private Map<String, String> symbolTableValor;

  public Semantico(Token[] tokens) {
    this.tokens = Arrays.asList(tokens);
    this.index = 0;
    this.symbolTable = new HashMap<>();
    this.symbolTableValor = new HashMap<>();
  }

  public String getIntermedioCode(){
    String asemblerCode = ".data\n";
    for (Map.Entry<String, TokenType> entry : symbolTable.entrySet()) {
      System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
      if (entry.getValue() == TokenType.NUMBER) {
        asemblerCode += entry.getKey() + " dd "+symbolTableValor.get(entry.getKey())+"\n";
      } else if (entry.getValue() == TokenType.STRING) {
        asemblerCode += entry.getKey() + " db "+symbolTableValor.get(entry.getKey())+"\n";
      }
    }
    System.out.println(asemblerCode);
    return asemblerCode;
  }

  public List<Token> getTokens() {
    return tokens;
  }

  public void setTokens(List<Token> tokens) {
    this.tokens = tokens;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public static Map<String, TokenType> getSymbolTable() {
    return symbolTable;
  }

  public void setSymbolTable(Map<String, TokenType> symbolTable) {
    this.symbolTable = symbolTable;
  }

  public static Map<String, String> getSymbolTableValor() {
    return symbolTableValor;
  }

  public void setSymbolTableValor(Map<String, String> symbolTableValor) {
    this.symbolTableValor = symbolTableValor;
  }

  public boolean isSemanticallyCorrect() {
    System.out.println("isSemanticallyCorrect");
    if (iniBlock()) {
      System.out.println("Programa correcto");
      getIntermedioCode();
      return true;
    } else {
      System.out.println("Programa incorrecto");
      for (Map.Entry<String, TokenType> entry : symbolTable.entrySet()) {
        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
      }

      for (Map.Entry<String, String> entry : symbolTableValor.entrySet()) {
        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
      }
      //getIntermedioCode();
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
      if (isAsignacionInt(tokens.get(index-1).getValor())) {
        symbolTable.put(tokens.get(index - 3).getValor(), TokenType.NUMBER);
        symbolTableValor.put(tokens.get(index - 3).getValor(), tokens.get(index - 1).getValor());
      }else{
        throw new RuntimeException("Error: Expected NUMBER but found STRING at " + index);
      }
    }
    //print symbol table
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
    if (!match(TokenType.OP)){
      return false;
    }
    if (match(TokenType.ASIGNACION)) {
      if (isAsignacionString(tokens.get(index-1).getValor())) {
        symbolTable.put(tokens.get(index - 3).getValor(), TokenType.STRING);
        symbolTableValor.put(tokens.get(index - 3).getValor(), tokens.get(index - 1).getValor());
      }else {
        throw new RuntimeException("Error: Expected STRING but found NUMBER at " + index);
      }
    }
    return match(TokenType.PUNTOYCOMA);
  }
  


  private boolean assignment(){
    TokenType temp = symbolTable.get(tokens.get(index).getValor());
    System.out.println("-------------------"+temp);
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

  private boolean expression(TokenType tipo) {
    TokenType temp = null;
    if (!factor()) {
      return false;
    }else{
      if (tokens.get(index-1).getTipo() == TokenType.ASIGNACION) {
        if (isAsignacionString(tokens.get(index-1).getValor())) {
          temp = TokenType.STRING;
        }else{
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
