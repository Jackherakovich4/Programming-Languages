package com.funabet.lexicalAnalysis;

public enum TokenType {
    // Keywords
    INTEGER_KEYWORD,
    STRING_KEYWORD,
    BOOL_KEYWORD,
    TRUE_KEYWORD,
    FALSE_KEYWORD,
    PRINT_KEYWORD,
    Ø,
    Ò,
    EOF,
    A_CIRCLE_LIST,



    //Single-Character Tokens
    A_CIRCLE,
    COMMA,
    RIGHT_ARROW,
    DOUBLE_RIGHT_ARROW,
    LEFT_ARROW,
    INFINITY,
    SQUIGGLE_EQUALS,
    DIAMOND,
    UPSIDEDOWN_EXCLAMATION,
    SEMI,
    O_PAREN,
    C_PAREN,
    UPSIDEDOWN_QUESTION,
    QUESTION,
    CAPITAL_PI,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_OR_EQUAL,
    LESS_THAN_OR_EQUAL,
    DIVIDE,
    TIMES,
    MODULUS,

    //One or Two Character Tokens
    PLUS,
    PLUSPLUS,
    MINUS,
    MINUSMINUS,
    EQUALSASSIGN,
    EQUALSCOMPARISON,

    //Literals
    INTEGER,
    DOUBLE,
    IDENTIFIER,
    STRING,



}
