package com.appineco.compiler.core.nodes;

/**
 * Read Statement
 */
public class ReadNode implements ASTNode {
  public String varName;

  public ReadNode(String varName) {
    this.varName = varName;
  }
}
