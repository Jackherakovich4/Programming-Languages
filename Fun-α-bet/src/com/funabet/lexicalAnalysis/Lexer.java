package com.funabet.lexicalAnalysis;

import java.util.ArrayList;
import java.util.HashMap;


public class Lexer {
    //Instance Variables
    private final String source;
    public final ArrayList<Lexeme> lexemes;
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
        keywords.put("bool", TokenType.BOOL_KEYWORD);
        keywords.put("true", TokenType.TRUE_KEYWORD);
        keywords.put("false", TokenType.FALSE_KEYWORD);
        keywords.put("print", TokenType.PRINT_KEYWORD);
        keywords.put("double", TokenType.REAL_KEYWORD);
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
    char c = advance();
    switch (c) {
        case ' ':
        case '\n':
        case '\r':
        case '\t':
            return null;
            //single char lexemes
        case '¡':
            return new Lexeme(TokenType.UPSIDEDOWN_EXCLAMATION, lineNumber);
        case ',':
            return new Lexeme(TokenType.COMMA, lineNumber);
        case '≈':
            return new Lexeme(TokenType.SQUIGGLE_EQUALS, lineNumber);
        case '◊':
            return new Lexeme(TokenType.DIAMOND, lineNumber);
        case 'Å':
            return new Lexeme(TokenType.A_CIRCLE, lineNumber);
        case '∞':
            return new Lexeme(TokenType.INFINITY, lineNumber);
        case '›':
            return new Lexeme(TokenType.RIGHT_ARROW, lineNumber);
        case '»':
            return new Lexeme(TokenType.DOUBLE_RIGHT_ARROW, lineNumber);
        case '‹':
            return new Lexeme(TokenType.LEFT_ARROW, lineNumber);
        case ';':
            return new Lexeme(TokenType.SEMI, lineNumber);
        case 'Ø':
            return new Lexeme(TokenType.Ø, lineNumber);
        case 'Ò':
            return new Lexeme(TokenType.Ò, lineNumber);
        case '(':
            return new Lexeme(TokenType.O_PAREN, lineNumber);
        case ')':
            return new Lexeme(TokenType.C_PAREN, lineNumber);
        case '?':
            return new Lexeme(TokenType.QUESTION, lineNumber);
        case '¿':
            return new Lexeme(TokenType.UPSIDEDOWN_QUESTION, lineNumber);
        case '∏':
            return new Lexeme(TokenType.CAPITAL_PI, lineNumber);
        case '>':
            return new Lexeme(TokenType.GREATER_THAN, lineNumber);
        case '≥':
            return new Lexeme(TokenType.GREATER_THAN_OR_EQUAL, lineNumber);
        case '<':
            return new Lexeme(TokenType.LESS_THAN, lineNumber);
        case '≤':
            return new Lexeme(TokenType.LESS_THAN_OR_EQUAL, lineNumber);
        case '/':
            return new Lexeme(TokenType.DIVIDE, lineNumber);
        case '*':
            return new Lexeme(TokenType.TIMES, lineNumber);
        case '%':
            return new Lexeme(TokenType.MODULUS, lineNumber);
            //one or two char
        case '=':
            if (match('=')) return new Lexeme(TokenType.EQUALSCOMPARISON, lineNumber);
            else return new Lexeme(TokenType.EQUALSASSIGN, lineNumber);
        case '+':
            if (match('+')) return new Lexeme(TokenType.PLUSPLUS, lineNumber);
            else return new Lexeme(TokenType.PLUS, lineNumber);
        case '-':
            if (match('-')) return new Lexeme(TokenType.MINUSMINUS, lineNumber);
            else return new Lexeme(TokenType.MINUS, lineNumber);
            //Strings
        case '"':
            return lexString();

        default:
            if (isDigit(c)) return lexNumber();
            else if (isAlpha(c)) return lexIdentifierOrKeyword();
            else error(lineNumber, "Unexpected Character: " +c);


    }
    return null;
    }

    private Lexeme lexNumber() {
    boolean isInteger=true;
    while (isDigit(peek())) advance();
    if (peek()=='.') {
        if (!isDigit(peekNext())) error(lineNumber, "Improper creation of real number (ends in decimal point)");
        isInteger=false;
        advance();
        while (isDigit(peek())) advance();
    }
    String numberString=source.substring(startOfCurrentLexeme, currentPosition);
    if (isInteger) {
        int number = Integer.parseInt(numberString);
        return new Lexeme(TokenType.INTEGER,  lineNumber, number);
    } else {
        double number = Double.parseDouble(numberString);
        return new Lexeme(TokenType.DOUBLE, lineNumber, number);
    }
    }

    private Lexeme lexString() {
        while (!(peek()=='"')) if(isAtEnd()) {  error(lineNumber, "unfinished string");
        return null;}
        else {advance();}
        advance();
        return new Lexeme(TokenType.STRING, lineNumber, source.substring(startOfCurrentLexeme+1,currentPosition-1));
        //possible issue now TODO
    }

    private Lexeme lexIdentifierOrKeyword() {
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(startOfCurrentLexeme, currentPosition);
        TokenType type = keywords.get(text);
        if (type==null) {
            return new Lexeme(TokenType.IDENTIFIER, lineNumber, text);
        } else {
            return new Lexeme(type, lineNumber);
        }
    }


//error reporting
    private void error(int lineNumber, String str) {
    System.out.println(str + " " + lineNumber);
    }


    // Printing
    public void printLexemes() {
    System.out.println("Lexemes found");
     for (Lexeme lexeme : lexemes) {
         System.out.println(lexeme);
     }
    }
}
