package com.funabet.Recognizer;

import com.funabet.Funalphabet;
import com.funabet.lexicalAnalysis.Lexer;
import com.funabet.lexicalAnalysis.Lexeme;
import com.funabet.lexicalAnalysis.TokenType;
import java.util.ArrayList;

public class recognizer {

    private int nextLexemeIndex=0;
    private final boolean printDebugMessages=true;
    private final ArrayList<Lexeme> lexemes;
    private Lexeme currentLexeme;

    //helper functiony things

    private void consume(TokenType expected) {
        if(check(expected)) advance();
        else error("Expected: " + expected + "but found " + currentLexeme + "instead.");
    }

    public recognizer(ArrayList<Lexeme> lexemes) {
        this.lexemes = lexemes;
        this.advance();
    }

    public void advance() {
        currentLexeme = lexemes.get(nextLexemeIndex);
        nextLexemeIndex++;
    }

    public boolean check(TokenType expected) {
        return currentLexeme.getType() == expected;
    }

    public boolean checkNext(TokenType expected) {
        return lexemes.get(nextLexemeIndex).getType() == expected;
    }

    //consumption functs

    public void program() {
        log("program");
        if (statementListPending()) statementList();
    }

    public void statementList() {
        log("statementList");
        statement();
        if (statementPending())
    }


    //pending functions


    public boolean programPending() {
        return statementListPending();
    }

    public boolean statementListPending() {
        return (statementPending() && statementListPending())|| statementPending() ;
    }

    public boolean blockPending() {
        return check(TokenType.UPSIDEDOWN_QUESTION);
    }



    public boolean statementPending() {
        return functionPending() || variableInitializationPending() || assignmentPending() || forLoopPending() ||
                whileLoopPending() || infiniteLoopPending()
                || expressionPending()
                || ifStatementPending()
                || arrayInitializationPending()
                || arrayListInitializationPending()
                || functionCallPending();
    }

    public boolean functionPending() {
        return check(TokenType.CAPITAL_PI);
    }

    public boolean variableInitializationPending() {
        return check(TokenType.UPSIDEDOWN_EXCLAMATION);
    }

    public boolean assignmentPending() {
        return check(TokenType.IDENTIFIER) && checkNext(TokenType.EQUALSASSIGN);
    }

    public boolean forLoopPending() {
        return check(TokenType.SQUIGGLE_EQUALS);
    }

    public boolean whileLoopPending() {
        return check(TokenType.DIAMOND);
    }

    public boolean infiniteLoopPending() {
        return check(TokenType.INFINITY);
    }

    public boolean ifStatementPending() {
        return check(TokenType.RIGHT_ARROW);
    }

    public boolean arrayInitializationPending() {
        return check(TokenType.A_CIRCLE);
    }

    public boolean arrayListInitializationPending() {
        return check(TokenType.A_CIRCLE_LIST);
    }

    public boolean functionCallPending() {
        return check(TokenType.IDENTIFIER) && checkNext(TokenType.O_PAREN);
    }

    public boolean expressionPending() {
        return ((check(TokenType.IDENTIFIER)|| check(TokenType.INTEGER) )&& (checkNext(TokenType.PLUS) || checkNext(TokenType.MINUS) || checkNext(TokenType.TIMES) || checkNext(TokenType.DIVIDE) || checkNext(TokenType.TIMES)))
            || ((check(TokenType.IDENTIFIER)|| check(TokenType.INTEGER) )&& (checkNext(TokenType.PLUSPLUS) || checkNext(TokenType.MINUSMINUS)))
                || check(TokenType.MINUS)
    || primaryPending();
    }

   public boolean primaryPending() {
        return check(TokenType.IDENTIFIER)|| check(TokenType.INTEGER) || check(TokenType.STRING);
    }

    public boolean parameterPending() {
        return  (check(TokenType.IDENTIFIER) && checkNext(TokenType.COMMA)) ||check(TokenType.IDENTIFIER);
    }

    public boolean elseStatementPending() {
        return check(TokenType.LEFT_ARROW);
    }

    public boolean comparisonPending() {
        return (primaryPending() && comparatorPendingNext()) || check(TokenType.IDENTIFIER);
    }

    public boolean comparatorPendingNext() {
        return checkNext(TokenType.EQUALSCOMPARISON) || checkNext(TokenType.LESS_THAN) || checkNext(TokenType.LESS_THAN_OR_EQUAL) || checkNext(TokenType.GREATER_THAN) || checkNext(TokenType.GREATER_THAN_OR_EQUAL);
    }

    public boolean unaryOperatorPending() {
        return check(TokenType.PLUSPLUS) || check(TokenType.MINUSMINUS);
    }

    public boolean binaryOperatorPending() {
        return check(TokenType.PLUS) || check(TokenType.MINUS) || check(TokenType.TIMES) || check(TokenType.DIVIDE) || check(TokenType.MODULUS);
    }

    public boolean booleanPending() {
        return check(TokenType.TRUE_KEYWORD) || check(TokenType.FALSE_KEYWORD) || check(TokenType.IDENTIFIER);
    }

    public boolean printStatementPending() {
        return check(TokenType.PRINT_KEYWORD);
    }

    private void log(String message) {
        if (printDebugMessages) System.out.println(message);
    }

    private void error(String message) {
        Funalphabet.syntaxError(message, currentLexeme);
    }

















}
