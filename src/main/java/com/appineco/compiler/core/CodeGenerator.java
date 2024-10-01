package com.appineco.compiler.core;

import com.appineco.compiler.core.nodes.ASTNode;
import com.appineco.compiler.core.nodes.IfNode;
import com.appineco.compiler.core.nodes.PrintNode;
import com.appineco.compiler.core.nodes.ProgramNode;
import com.appineco.compiler.core.nodes.ReadNode;
import com.appineco.compiler.core.nodes.VarDeclNode;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.*;

/**
 * Generates bytecode from the AST
 */
public class CodeGenerator {
  // Holds the root of the AST, which represents the entire program to be compiled into bytecode.
  private ProgramNode program;
  // An instance of ASM's ClassWriter used to write the bytecode for the new class
  private ClassWriter classWriter;
  // An instance of ASM's MethodVisitor used to generate bytecode for methods within the class
  private MethodVisitor methodVisitor;
  // A map to keep track of variable names and their corresponding local variable indices in the generated bytecode
  private Map<String, Integer> variables;

  public CodeGenerator(ProgramNode program) {
    this.program = program;
    // Using COMPUTE_FRAMES for automatic calculation of Stack Map Frames
    this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    this.variables = new HashMap<>();
  }

  /**
   * Generates the bytecode for the entire program and returns it as a byte array
   */
  public byte[] generate() {
    // Define the class Main with Java version 1.8, public access, and java/lang/Object as its superclass.
    classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "Main", null, "java/lang/Object", null);

    // A default constructor <init> for the class
    MethodVisitor constructor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
    constructor.visitCode();
    constructor.visitVarInsn(Opcodes.ALOAD, 0);
    constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
    constructor.visitInsn(Opcodes.RETURN);
    constructor.visitMaxs(0, 0);
    constructor.visitEnd();

    // The main method with public and static access modifiers.
    methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
    methodVisitor.visitCode();

    // Initialization a new Scanner object to read input from System.in
    methodVisitor.visitTypeInsn(Opcodes.NEW, "java/util/Scanner");
    methodVisitor.visitInsn(Opcodes.DUP);
    methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
    methodVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
    // reserving index 1 for the Scanner
    methodVisitor.visitVarInsn(Opcodes.ASTORE, 1);

    // Start code generation for the program. Iterates over the statements in the program's AST
    int varIndex = 2; // Index for user variables
    for (ASTNode stmt : program.statements) {
      generateStatement(stmt, varIndex);
    }

    // Method Closure
    methodVisitor.visitInsn(Opcodes.RETURN);
    methodVisitor.visitMaxs(0, 0);
    methodVisitor.visitEnd();

    classWriter.visitEnd();

    return classWriter.toByteArray();
  }

  /**
   * Generates bytecode for individual statements in the AST.
   *
   * @param node individual statements
   * @param varIndex variable indexing
   */
  private void generateStatement(ASTNode node, int varIndex) {
    if (node instanceof VarDeclNode) {
      VarDeclNode varDecl = (VarDeclNode) node;
      variables.put(varDecl.varName, varIndex++);
    } else if (node instanceof ReadNode) {
      ReadNode readNode = (ReadNode) node;
      Integer index = variables.get(readNode.varName);
      if (index == null) {
        throw new RuntimeException("Variable " + readNode.varName + " not declared");
      }

      // Generates bytecode to read an integer from the Scanner
      methodVisitor.visitVarInsn(Opcodes.ALOAD, 1); // Scanner
      methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Scanner", "nextInt", "()I", false);
      methodVisitor.visitVarInsn(Opcodes.ISTORE, index);
    } else if (node instanceof PrintNode) {
      PrintNode printNode = (PrintNode) node;
      Integer index = variables.get(printNode.varName);
      if (index == null) {
        throw new RuntimeException("Variable " + printNode.varName + " not declared");
      }

      // Generates bytecode to print an integer to the console
      methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
      methodVisitor.visitVarInsn(Opcodes.ILOAD, index);
      methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
    } else if (node instanceof IfNode) {
      IfNode ifNode = (IfNode) node;
      Integer index = variables.get(ifNode.varName);
      if (index == null) {
        throw new RuntimeException("Variable " + ifNode.varName + " not declared");
      }

      // ASM's Label class is used to mark positions in the bytecode for jump instructions
      Label elseLabel = new Label();
      Label endLabel = new Label();

      // Generates bytecode for the condition check
      methodVisitor.visitVarInsn(Opcodes.ILOAD, index);
      methodVisitor.visitJumpInsn(Opcodes.IFEQ, elseLabel);

      // True branch
      for (ASTNode stmt : ifNode.trueBranch) {
        generateStatement(stmt, varIndex);
      }
      methodVisitor.visitJumpInsn(Opcodes.GOTO, endLabel);

      // Else label
      methodVisitor.visitLabel(elseLabel);

      // We can add support for the else branch here
      // Let's skip it for now

      // End of if label
      methodVisitor.visitLabel(endLabel);
    }
  }
}
