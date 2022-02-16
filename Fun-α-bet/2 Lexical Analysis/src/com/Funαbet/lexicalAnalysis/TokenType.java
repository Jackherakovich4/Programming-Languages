package com.Funαbet.lexicalAnalysis;

public enum TokenType {
    // Keywords
    INTEGER_KEYWORD,
    STRING_KEYWORD,
    Ø,
    Ò,
    EOF,



    //Single-Character Tokens
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
