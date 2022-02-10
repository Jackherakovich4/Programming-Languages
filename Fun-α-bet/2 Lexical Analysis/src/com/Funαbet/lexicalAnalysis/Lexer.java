package com.Funαbet.lexicalAnalysis;

import java.util.ArrayList;

public class Lexer {
    //Instance Variables
    private final String source;
    private final ArrayList<Lexeme> lexemes;



    //Constructor
public Lexer(String source) {
    this.source=source;
    this.lexemes=new ArrayList<>();
}





    //Core Helper Methods
    private void skipWhitespace() {

    }


    // Main lex() function
    public ArrayList<Lexeme> lex() {
        return this.lexemes;
    }

    // Smaller lexing functions called by lex()

    // Printing
    public void printLexemes() {
    System.out.println("Lexemes found");
     for (Lexeme lexeme : lexemes) {
         System.out.println(lexeme);
     }
    }
}
