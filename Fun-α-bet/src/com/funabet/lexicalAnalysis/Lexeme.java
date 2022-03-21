package com.funabet.lexicalAnalysis;

public class Lexeme {
    // --------------- Instance Variables ---------------
    // Every com.Funαbet.com.funalphabet.lexicalAnalysis.com.funabet.lexicalAnalysis.Lexeme has both of these:
    private TokenType type;
    private int lineNumber;

    // Type-Value Lexemes also get ONE of these:
    private Integer numberval;
    private String stringval;
    private Lexeme left;
    private Lexeme right;
    private Double realval;
    private Boolean boolval;


    // --------------- Constructors ---------------
    public Lexeme (TokenType type, int lineNumber, Integer numberval) {
        this.type=type;
        this.lineNumber=lineNumber;
        this.numberval=numberval;
    }

    public Lexeme (TokenType type, int lineNumber, String stringval) {
        this.type=type;
        this.lineNumber=lineNumber;
        this.stringval=stringval;
    }


    public Lexeme (TokenType type, int lineNumber) {
        this.type=type;
        this.lineNumber=lineNumber;
    }

    public Lexeme (TokenType type, int lineNumber, Double realval) {
        this.type=type;
        this.lineNumber=lineNumber;
        this.realval=realval;
    }

    public Lexeme (TokenType type, int lineNumber, Boolean boolval) {
        this.type=type;
        this.lineNumber=lineNumber;
        this.boolval=boolval;
    }


    // --------------- Getters & Setters ---------------


    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLeft(Lexeme left) {
        this.left=left;
    }

    public void setRight(Lexeme right) {
        this.left=right;
    }

    public Integer getNumberval() {
        return numberval;
    }

    public String getStringval() {
        return stringval;
    }

    public Boolean getBoolval() {
        return boolval;
    }

    public TokenType getType() {
        return type;
    }

    public Double getRealval() {
        return realval;
    }

    // --------------- toString ---------------
    public String toString() {
        if (!(this.getBoolval()==(null))) {
            return "type " + type + ", in line number " + lineNumber + ", and of value " + this.getBoolval();
        } else if (!(this.getRealval()==(null))) {
            return "type " + type + ", in line number " + lineNumber + ", and of value " + this.getRealval();
        } else if (!(this.getStringval()==(null))) {
            return "type " + type + ", in line number " + lineNumber + ", and of value " + this.getStringval();
        } else if (!(this.getNumberval()==(null))) {
            return "type " + type + ", in line number " + lineNumber + ", and of value "+  this.getNumberval();
         } else {
            return " type " + type + ", in line number " + lineNumber;
        }

    }
    // TODO - Write a basic toString method that prints
    //  the com.Funαbet.com.funalphabet.lexicalAnalysis.com.funabet.lexicalAnalysis.Lexeme's type, line number, and value (if there is one)



}
