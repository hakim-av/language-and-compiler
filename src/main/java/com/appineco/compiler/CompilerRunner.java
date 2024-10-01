package com.appineco.compiler;

import com.appineco.compiler.core.CodeGenerator;
import com.appineco.compiler.core.Lexer;
import com.appineco.compiler.core.Parser;
import com.appineco.compiler.core.nodes.ProgramNode;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Orchestrates the compilation process
 */
public class CompilerRunner {
  public static void main(String[] args) throws IOException {

    // Reading source code from file
    String sourceCode = readFile("src/main/java/com/appineco/compiler/language/OwnLanguageExample.appineco");

    // Lexical analysis
    Lexer lexer = new Lexer(sourceCode);

    // Syntax analysis
    Parser parser = new Parser(lexer);
    ProgramNode program = parser.parse();

    // Bytecode generation
    CodeGenerator generator = new CodeGenerator(program);
    byte[] byteCode = generator.generate();

    // Saving bytecode to a file
    java.nio.file.Files.write(
        java.nio.file.Paths.get("src/main/java/com/appineco/compiler/generated/Main.class"),
        byteCode);
  }

  private static String readFile(String filePath) throws IOException {
    FileInputStream fis = new FileInputStream(filePath);
    byte[] data = fis.readAllBytes();
    fis.close();
    return new String(data);
  }
}
