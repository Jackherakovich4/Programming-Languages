package com.Funαbet.lexicalAnalysis;

public class LexerTest {

    public static void main(String[] args) {
        Lexer lexer = new Lexer ("int x = 10;");
        lexer.lex();
        lexer.printLexemes();
    }

}
