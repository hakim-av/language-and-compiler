package com.appineco.compiler.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Tokenizes the input source code
 */
public class Lexer {
  // Holds the input source code as a string that the lexer will analyze.
  private String input;
  // Represents the current position (index) in the input string that the lexer is analyzing.
  private int pos;
  // Stores the character at the current position in the input string. If the end of the input is reached, it is set to '\0' (null character).
  private char currentChar;

  private static final Map<String, TokenType> RESERVED_WORDS;
  static {
    RESERVED_WORDS = new HashMap<>();
    RESERVED_WORDS.put("function", TokenType.FUNCTION);
    RESERVED_WORDS.put("var", TokenType.VAR);
    RESERVED_WORDS.put("if", TokenType.IF);
    RESERVED_WORDS.put("else", TokenType.ELSE);
    RESERVED_WORDS.put("while", TokenType.WHILE);
    RESERVED_WORDS.put("print", TokenType.PRINT);
    RESERVED_WORDS.put("read", TokenType.READ);
    RESERVED_WORDS.put("return", TokenType.RETURN);
    RESERVED_WORDS.put("int", TokenType.INT);
    RESERVED_WORDS.put("end", TokenType.END);
  }

  public Lexer(String input) {
    this.input = input;
    this.pos = 0;
    this.currentChar = input.length() > 0 ? input.charAt(0) : '\0';
  }

  /**
   * Retrieves the next token from the input stream.
   * @return a new Token with the appropriate TokenType
   */
  public Token getNextToken() {
    while (currentChar != '\0') {

      // Whitespace Handling
      if (Character.isWhitespace(currentChar)) {
        skipWhitespace();
        continue;
      }

      // Identifier or Reserved Word Handling
      if (Character.isLetter(currentChar)) {
        String id = identifier();
        TokenType type = RESERVED_WORDS.getOrDefault(id, TokenType.IDENTIFIER);
        return new Token(type, id);
      }

      // Number Handling
      if (Character.isDigit(currentChar)) {
        String num = number();
        return new Token(TokenType.NUMBER, num);
      }

      // Symbol Handling. Process symbols, if necessary
      advance();
    }

    // a new Token of type TokenType.EOF to indicate the end of input
    return new Token(TokenType.EOF, null);
  }

  /**
   * Moves the pos pointer to the next character in the input string and updates currentChar accordingly.
   */
  private void advance() {
    pos++;
    if (pos >= input.length()) {
      currentChar = '\0';
    } else {
      currentChar = input.charAt(pos);
    }
  }

  /**
   * Skips over any whitespace characters in the input to reach the next meaningful character.
   */
  private void skipWhitespace() {
    while (Character.isWhitespace(currentChar)) {
      advance();
    }
  }

  /**
   * Recognizes and extracts identifiers and reserved words from the input.
   * @return the accumulated string from result, which represents an identifier or a reserved word.
   */
  private String identifier() {
    StringBuilder result = new StringBuilder();
    while (Character.isLetterOrDigit(currentChar)) {
      result.append(currentChar);
      advance();
    }
    return result.toString();
  }

  /**
   * Recognizes and extracts numerical literals from the input.
   * @return returns the accumulated string from result, which represents a numerical literal.
   */
  private String number() {
    StringBuilder result = new StringBuilder();
    while (Character.isDigit(currentChar)) {
      result.append(currentChar);
      advance();
    }
    return result.toString();
  }
}
