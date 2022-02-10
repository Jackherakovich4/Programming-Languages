package com.Funαbet.lexicalAnalysis;

public class Lexeme {
    // --------------- Instance Variables ---------------
    // Every com.Funαbet.lexicalAnalysis.Lexeme has both of these:
    private TokenType type;
    private int lineNumber;

    // Type-Value Lexemes also get ONE of these:
    private int NUMBER;
    private String STRING;
    private String IDENTIFIER;
    private String KEYWORD;
    private String LITERAL;


    // --------------- Constructors ---------------
    public Lexeme(TokenType type, int lineNumber) {
        this.type=type;
        this.lineNumber=lineNumber;
    }

    public Lexeme(TokenType type, int lineNumber, int NUMBER) {
        this.type=type;
        this.lineNumber=lineNumber;
        this.NUMBER=NUMBER;
    }

    public Lexeme(TokenType type, int lineNumber, String STRING) {
        this.type=type;
        this.lineNumber=lineNumber;
        this.STRING=STRING;
    }
    /**
    public com.Funαbet.lexicalAnalysis.Lexeme(com.Funαbet.lexicalAnalysis.TokenType type, int lineNumber, String IDENTIFIER) {
        this.type=type;
        this.lineNumber=lineNumber;
        this.IDENTIFIER=IDENTIFIER;
    }

    public com.Funαbet.lexicalAnalysis.Lexeme(com.Funαbet.lexicalAnalysis.TokenType type, int lineNumber, String KEYWORD) {
        this.type=type;
        this.lineNumber=lineNumber;
        this.KEYWORD=KEYWORD;
    }

    public com.Funαbet.lexicalAnalysis.Lexeme(com.Funαbet.lexicalAnalysis.TokenType type, int lineNumber, String LITERAL) {
        this.type=type;
        this.lineNumber=lineNumber;
        this.LITERAL=LITERAL;
    }
     */

    // --------------- Getters & Setters ---------------

    public int getNUMBER() {
        return NUMBER;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getIDENTIFIER() {
        return IDENTIFIER;
    }

    public String getKEYWORD() {
        return KEYWORD;
    }

    public String getLITERAL() {
        return LITERAL;
    }

    public String getSTRING() {
        return STRING;
    }

    public void setIDENTIFIER(String IDENTIFIER) {
        this.IDENTIFIER = IDENTIFIER;
    }

    public void setKEYWORD(String KEYWORD) {
        this.KEYWORD = KEYWORD;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public TokenType getType() {
        return type;
    }

    public void setLITERAL(String LITERAL) {
        this.LITERAL = LITERAL;
    }

    public void setNUMBER(int NUMBER) {
        this.NUMBER = NUMBER;
    }

    public void setSTRING(String STRING) {
        this.STRING = STRING;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    // --------------- toString ---------------
    public String toString() {
        if (this.getIDENTIFIER() != null) {
            return "this is a com.Funαbet.lexicalAnalysis.Lexeme of type " + type + ", in line number " + lineNumber + ", and of value " + IDENTIFIER;
        } else if (this.getSTRING() != null) {
            return "this is a com.Funαbet.lexicalAnalysis.Lexeme of type " + type + ", in line number " + lineNumber + ", and of value " + STRING;
        } else if (!(this.getNUMBER() != null) {
            return "this is a com.Funαbet.lexicalAnalysis.Lexeme of type " + type + ", in line number " + lineNumber + ", and of value " + NUMBER;
        } else if (this.getKEYWORD() != null) {
            return "this is a com.Funαbet.lexicalAnalysis.Lexeme of type " + type + ", in line number " + lineNumber + ", and of value " + KEYWORD;
        } else if (this.getLITERAL() != null) {
            return "this is a com.Funαbet.lexicalAnalysis.Lexeme of type " + type + ", in line number " + lineNumber + ", and of value "+  LITERAL;
         } else {
            return "this is a com.Funαbet.lexicalAnalysis.Lexeme of type " + type + ", in line number " + lineNumber;
        }

    }
    // TODO - Write a basic toString method that prints
    //  the com.Funαbet.lexicalAnalysis.Lexeme's type, line number, and value (if there is one)



}
