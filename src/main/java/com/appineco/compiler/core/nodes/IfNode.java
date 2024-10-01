package com.appineco.compiler.core.nodes;

import java.util.List;

/**
 * If Statement
 */
public class IfNode implements ASTNode {
  public String varName;
  public List<ASTNode> trueBranch;

  public IfNode(String varName, List<ASTNode> trueBranch) {
    this.varName = varName;
    this.trueBranch = trueBranch;
  }
}
