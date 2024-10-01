Project Structure:
- The core package contains the compiler code
- The language package contains sample code written in our own language that we just came up with. 
The `.appineco` extension is our own extension, which is based on the domain name of the personal 
website of the owner of this repository. You can use your own extension
- The generated package contains the compiled byte code

Core Package Structure:
- Lexer: Tokenizes the input source code.
- Parser: Parses tokens into an abstract syntax tree (AST).
- AST Nodes: Represents the structure of the parsed code.
- CodeGenerator: Generates bytecode from the AST.
- CompilerRunner: Orchestrates the compilation process.

**Please note that at the moment the program can only accept integer numbers as input!!!**

The CompilerRunner class serves as the entry point for the compiler. It reads the source code from a file, 
performs lexical and syntax analysis, generates the bytecode, and writes it to a `.class` file.

Update the file paths in the CompilerRunner class to match your project structure or make them relative paths:
Reading source code from file / Saving bytecode to a file.

The generated `Main.class` file contains the bytecode that can be executed using the Java Virtual Machine:
`java Main`

The CodeGenerator class uses the ASM library (`org.objectweb.asm.*`).

Understanding the ASM Opcodes Used:
- `NEW,` `DUP`: Allocate new objects and duplicate the top of the stack, typically used when calling constructors.
- `GETSTATIC`: Access static fields, such as `System.in` or `System.out`. 
- `INVOKESPECIAL`, `INVOKEVIRTUAL`:Call methods; `INVOKESPECIAL` for constructors and private methods, 
`INVOKEVIRTUAL` for instance methods.
- `ALOAD`, `ASTORE`, `ILOAD`, `ISTORE`: Load and store references (A) or integers (I) from and to local variables. 
- `IFEQ`, `GOTO`: Conditional and unconditional jump instructions for control flow. 
- `Label`: Mark positions in the bytecode for jumps and branching. 
- `RETURN`: Return from a method; `RETURN` for methods returning void.