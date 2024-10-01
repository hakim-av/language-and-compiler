package com.appineco.compiler.core.nodes;

/**
 * Variable Declaration Statement
 */
public class VarDeclNode implements ASTNode {
  public String varName;

  public VarDeclNode(String varName) {
    this.varName = varName;
  }
}

