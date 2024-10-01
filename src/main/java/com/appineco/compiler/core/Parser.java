package com.appineco.compiler.core;

import com.appineco.compiler.core.nodes.ASTNode;
import com.appineco.compiler.core.nodes.IfNode;
import com.appineco.compiler.core.nodes.PrintNode;
import com.appineco.compiler.core.nodes.ProgramNode;
import com.appineco.compiler.core.nodes.ReadNode;
import com.appineco.compiler.core.nodes.VarDeclNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses tokens into an abstract syntax tree (AST).
 */
public class Parser {
  // Holds an instance of the Lexer class, which provides the stream of tokens to be parsed
  private Lexer lexer;
  // Stores the current token being examined
  private Token currentToken;

  public Parser(Lexer lexer) {
    this.lexer = lexer;
    this.currentToken = lexer.getNextToken();
  }

  /**
   * Initiates the parsing process and constructs the AST representing the entire program.
   *
   * @return a new ProgramNode containing the list of statement nodes
   */
  public ProgramNode parse() {
    List<ASTNode> statements = new ArrayList<>();
    while (currentToken.type != TokenType.EOF) {
      statements.add(statement());
    }
    return new ProgramNode(statements);
  }

  /**
   * Parses a single statement from the input tokens and returns its corresponding AST node.
   *
   * @return an ASTNode representing the parsed statement
   */
  private ASTNode statement() {
    if (currentToken.type == TokenType.VAR) {
      // Variable Declaration
      eat(TokenType.VAR);
      String varName = currentToken.value;
      eat(TokenType.IDENTIFIER);
      return new VarDeclNode(varName);
    } else if (currentToken.type == TokenType.READ) {
      // Read Statement
      eat(TokenType.READ);
      String varName = currentToken.value;
      eat(TokenType.IDENTIFIER);
      return new ReadNode(varName);
    } else if (currentToken.type == TokenType.PRINT) {
      // Print Statement
      eat(TokenType.PRINT);
      String varName = currentToken.value;
      eat(TokenType.IDENTIFIER);
      return new PrintNode(varName);
    } else if (currentToken.type == TokenType.IF) {
      // If Statement
      // The parser currently does not support else branches in if statements
      eat(TokenType.IF);
      String varName = currentToken.value;
      eat(TokenType.IDENTIFIER);
      List<ASTNode> trueBranch = new ArrayList<>();
      while (currentToken.type != TokenType.END) {
        trueBranch.add(statement());
      }
      eat(TokenType.END);
      return new IfNode(varName, trueBranch);
    } else {
      // Error Handling
      throw new RuntimeException("Syntax Error: Unexpected token " + currentToken.type);
    }
  }

  /**
   * Validates that the current token matches the expected token type and advances to the next token.
   * Used throughout the parsing methods to ensure that the sequence of tokens matches the expected grammar.
   *
   * @param type the expected token type
   */
  private void eat(TokenType type) {
    if (currentToken.type == type) {
      currentToken = lexer.getNextToken();
    } else {
      throw new RuntimeException("Syntax Error: Expected " + type + " but found " + currentToken.type);
    }
  }
}

