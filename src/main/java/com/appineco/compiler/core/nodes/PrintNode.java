package com.appineco.compiler.core.nodes;

/**
 * Print Statement
 */
public class PrintNode implements ASTNode {
  public String varName;

  public PrintNode(String varName) {
    this.varName = varName;
  }
}
