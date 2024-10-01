package com.appineco.compiler.core.nodes;

import java.util.List;

/**
 * The root of the AST
 */
public class ProgramNode implements ASTNode {
  public List<ASTNode> statements;

  public ProgramNode(List<ASTNode> statements) {
    this.statements = statements;
  }
}

