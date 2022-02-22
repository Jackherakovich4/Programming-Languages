package com.funabet;


import com.funabet.lexicalAnalysis.Lexeme;
import com.funabet.lexicalAnalysis.Lexer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Funalphabet {

    private static final ArrayList<String> syntaxErrorMessages = new ArrayList<>();
    private static final ArrayList<String> runtimeErrorMessages = new ArrayList<>();

    public static void syntaxError(String message, int lineNumber) {
        syntaxErrorMessages.add("Syntax error (line " + lineNumber + "):" + message );
    }

    public static void syntaxError(String message, Lexeme lexeme) {
        syntaxErrorMessages.add("Syntax error at " +lexeme + "):" + message );
    }

    public static void runtimeError(String message, int lineNumber) {
        runtimeErrorMessages.add("Syntax error (line " + lineNumber + "):" + message );
    }

    public static void runtimeError(String message, Lexeme lexeme) {
        runtimeErrorMessages.add("Syntax error at " + lexeme + "):" + message );
    }



    public static void main(String[] args) throws IOException {
        try {
            if (singlePathProvided(args)) runFile(args[0]);
            else {
                System.out.println("Usage: Funαbet [path to .alpha file]");
                System.exit(64);
            }
        } catch (IOException exception) {
            throw new IOException(exception.toString());
        }
    }

    public static boolean singlePathProvided(String [] x) {
        return x.length==1;
    }


    public static void runFile(String path) throws IOException {
        String sourceCode = getSourceCodeFromFile(path);
        run(sourceCode);
        //if (hadSyntaxError) System.exit(65);
        //if (hadRuntimeError) System.exit(70);
    }

    private static String getSourceCodeFromFile(String path) throws IOException {
        byte[] bytes= Files.readAllBytes(Paths.get(path));
        return new String(bytes, Charset.defaultCharset());
    }

    public static void run(String sourceCode) {
        Lexer lexer = new Lexer(sourceCode);
        ArrayList<Lexeme> lexemes = lexer.lex();
        lexer.printLexemes();
    }

}
