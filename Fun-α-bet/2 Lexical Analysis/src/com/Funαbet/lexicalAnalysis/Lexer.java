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
        keywords.put("String", TokenType.STRING_KEYWORD);
        keywords.put("¡", TokenType.UPSIDEDOWN_EXCLAMATION);
        keywords.put("¿", TokenType.UPSIDEDOWN_QUESTION);
        keywords.put("=", TokenType.EQUALSASSIGN);
        keywords.put("==", TokenType.EQUALSCOMPARISON);
        keywords.put(";", TokenType.SEMI);
        keywords.put("(", TokenType.O_PAREN);
        keywords.put(")", TokenType.C_PAREN);
        keywords.put("?", TokenType.QUESTION);
        keywords.put("∏", TokenType.CAPITAL_PI);
        keywords.put(">", TokenType.GREATER_THAN);
        keywords.put("≥", TokenType.GREATER_THAN_OR_EQUAL);
        keywords.put("<", TokenType.LESS_THAN);
        keywords.put("≤", TokenType.LESS_THAN_OR_EQUAL);
        keywords.put("/", TokenType.DIVIDE);
        keywords.put("*", TokenType.TIMES);
        keywords.put("%", TokenType.MODULUS);
        keywords.put("+", TokenType.PLUS);
        keywords.put("++", TokenType.PLUSPLUS);
        keywords.put("-", TokenType.MINUS);
        keywords.put("--", TokenType.MINUSMINUS);
        keywords.put("Ø", TokenType.Ø);
        keywords.put("Ò", TokenType.Ò);
        keywords.put("EOF", TokenType.EOF);
    return keywords;
    }





    //Core Helper Methods
    private void skipWhitespace() {

    }

    private boolean isAtEnd() {
     return currentPosition >= source.length();
    }

    private char peek() {
     if (isAtEnd()) {
        return '\0';
     }
     return source.charAt(currentPosition);
    }

    private char peekNext() {
        if (currentPosition+1>=source.length()) {
            return '\0';
        }
        return source.charAt(currentPosition+1);
    }

    private boolean match(char expected) {
     if (isAtEnd() || source.charAt(currentPosition) != expected) {
    return false;
        }
     currentPosition++;
        return true;
    }

    private char advance() {
    char currentChar = source.charAt(currentPosition);
    if(currentChar=='\n' || currentChar=='\r') lineNumber++;
    currentPosition++;
    return currentChar;
    }

    // Char Classification Methods

    private boolean isDigit(char c) {
    return c>='0' && c<='9';
    }

    private boolean isAlpha(char c) {
    return (c>='a' && c<='z') || (c>='A' && c<= 'Z') || c=='_' || c=='-';
    }

    private boolean isAlphaNumeric (char c) {return isAlpha(c)||isDigit(c);}





    // Main lex() function
    public ArrayList<Lexeme> lex() {
    while (!isAtEnd()) {
        startOfCurrentLexeme = currentPosition;
        Lexeme nextLexeme = getNextLexeme();
        if (nextLexeme != null) lexemes.add(nextLexeme);
    }
    lexemes.add(new Lexeme(TokenType.EOF, lineNumber));
    return lexemes;
    }

    // Smaller lexing functions called by lex()

    private Lexeme getNextLexeme() {
    
    }


    // Printing
    public void printLexemes() {
    System.out.println("Lexemes found");
     for (Lexeme lexeme : lexemes) {
         System.out.println(lexeme);
     }
    }
}
