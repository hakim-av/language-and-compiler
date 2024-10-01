package com.appineco.compiler.core;

/**
 * Represents the smallest units of meaning that the lexer extracts from the source code
 * during lexical analysis. Each token encapsulates both the type of the token
 * (e.g., identifier, number, keyword) and its actual value from the source code.
 */
public class Token {
  public TokenType type;
  public String value;

  public Token(TokenType type, String value) {
    this.type = type;
    this.value = value;
  }
}
