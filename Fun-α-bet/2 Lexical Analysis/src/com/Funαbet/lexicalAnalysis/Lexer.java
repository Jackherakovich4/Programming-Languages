package com.Funαbet.lexicalAnalysis;

import java.util.ArrayList;
import java.util.HashMap;


public class Lexer {
    //Instance Variables
    private final String source;
    private final ArrayList<Lexeme> lexemes;
    private final HashMap<String, TokenType> keywords;

    private int currentPosition;
    private int startOfCurrentLexeme;
    private int lineNumber;


    //Constructor
public Lexer(String source) {
    this.source=source;
    this.lexemes=new ArrayList<>();
    this.keywords=getKeywords();

    this.currentPosition=0;
    this.startOfCurrentLexeme=0;
    this.lineNumber=1;
}

// Populating Keywords
    private HashMap<String, TokenType> getKeywords(){
    HashMap<String, TokenType> keywords=new HashMap<>();
    keywords.put("int", TokenType.INTEGER_KEYWORD);
    return keywords;
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
