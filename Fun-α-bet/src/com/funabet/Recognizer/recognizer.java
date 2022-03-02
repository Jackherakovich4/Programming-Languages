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
        if (statementPending()) statement();
        if (statementListPending()) statementList();
    }

    public void statement() {
        log("statement");
        if (functionPending()) function();
        else if (functionCallPending()) functionCall();
        else if (variableInitializationPending()) variableInitialization();
        else if (assignmentPending()) assignment();
        else if (forLoopPending()) forLoop();
        else if (whileLoopPending()) whileLoop();
        else if (infiniteLoopPending()) infiniteLoop();
        else if (expressionPending()) expression();
        else if (ifStatementPending()) ifStatement();
        else if (arrayInitializationPending()) arrayInitialization();
        else if (arrayListInitializationPending()) arrayListInitialization();
        else error("expected statement");
    }

    public void functionCall() {
        consume(TokenType.IDENTIFIER);
        consume(TokenType.O_PAREN);
        if (functionInputPending()) functionInput();
        consume(TokenType.C_PAREN);
    }

    public void function() {
        log("function");
        consume(TokenType.CAPITAL_PI);
        consume(TokenType.IDENTIFIER);
        consume(TokenType.O_PAREN);
        if(parameterPending()) parameter();
        consume(TokenType.C_PAREN);
        block();
    }

    public void parameter() {
        log("parameter");
        if (functionPending()) function();
        else if (check(TokenType.IDENTIFIER)&&check(TokenType.COMMA)) { consume(TokenType.IDENTIFIER); consume(TokenType.COMMA); parameter(); }
        else if (check(TokenType.IDENTIFIER)) consume(TokenType.IDENTIFIER);
        else error("expected parameter");
    }

    public void block() {
        log("block");
        consume(TokenType.UPSIDEDOWN_QUESTION);
        statementList();
        consume(TokenType.QUESTION);
    }

    public void functionInput() {
        log("functionInput");
        if (primaryPending() && checkNext(TokenType.COMMA)) { primary(); consume(TokenType.COMMA); functionInput(); }
        else if (primaryPending()) primary();
        else error("expected function input");
    }

    public void primary() {
        log("primary");
        if (check(TokenType.INTEGER)) consume(TokenType.INTEGER);
        else if (check(TokenType.DOUBLE)) consume(TokenType.DOUBLE);
        else if (check(TokenType.STRING)) consume(TokenType.STRING);
        else if (booleanPending()) booleanx();
        else if (check(TokenType.IDENTIFIER)) consume(TokenType.IDENTIFIER);
        else error("primary expected");
    }

    public void booleanx(){
        log("boolean");
        if (check(TokenType.FALSE_KEYWORD)) consume(TokenType.FALSE_KEYWORD);
        else if (check(TokenType.TRUE_KEYWORD)) consume(TokenType.TRUE_KEYWORD);
        else if (comparisonPending()) comparison();
        else error("boolean expected");
    }

    public void comparison() {
        log("comparison");
        primary();
        comparator();
        primary();
    }

    public void comparator() {
        log("Comparator");
        if (check(TokenType.EQUALSCOMPARISON)) consume(TokenType.EQUALSCOMPARISON);
        else if (check(TokenType.GREATER_THAN_OR_EQUAL)) consume(TokenType.GREATER_THAN_OR_EQUAL);
        else if (check(TokenType.GREATER_THAN)) consume(TokenType.GREATER_THAN);
        else if (check(TokenType.LESS_THAN_OR_EQUAL)) consume(TokenType.LESS_THAN_OR_EQUAL);
        else if (check(TokenType.LESS_THAN)) consume(TokenType.LESS_THAN);
        else error("comparator expected");
    }

    public void variableInitialization() {
        log("variableInitialization");
        consume(TokenType.UPSIDEDOWN_EXCLAMATION);
        consume(TokenType.IDENTIFIER);
        consume(TokenType.EQUALSASSIGN);
        expression();
    }

    public void expression() {
        log("expression");
        if(check(TokenType.MINUS)) {consume(TokenType.MINUS); primary();}
        else if (primaryPending()) {
            if (checkNext(TokenType.PLUS)||checkNext(TokenType.MINUS)||checkNext(TokenType.TIMES)||checkNext(TokenType.DIVIDE)||checkNext(TokenType.MODULUS)) {
                primary();
                binaryOperator();
                expression();
            } else if (checkNext(TokenType.PLUSPLUS)||checkNext(TokenType.MINUSMINUS)) {
                primary();
                unaryOperator();
            } else {
                primary();
            }
        }
        else error("expression expected");
    }

    public void binaryOperator() {
        log("binary operator");
        if (check(TokenType.MINUS)) consume(TokenType.MINUS);
        else if (check(TokenType.PLUS)) consume(TokenType.PLUS);
        else if (check(TokenType.TIMES)) consume(TokenType.TIMES);
        else if (check(TokenType.DIVIDE)) consume(TokenType.DIVIDE);
        else if (check(TokenType.MODULUS)) consume(TokenType.MODULUS);
        else error("binary Operator expected");
    }

    public void unaryOperator() {
        log("unary Operator");
        if (check(TokenType.MINUSMINUS)) consume(TokenType.MINUSMINUS);
        else if (check(TokenType.PLUSPLUS)) consume(TokenType.PLUSPLUS);
        else error("unary Operator expected");
    }

    public void assignment() {
        log("assignment");
        consume(TokenType.IDENTIFIER);
        if (check(TokenType.EQUALSASSIGN)) { consume(TokenType.EQUALSASSIGN); expression();}
        else if (unaryOperatorPending()) unaryOperator();
        else error("variable assignment expected");
    }

    public void forLoop() {
        log("forLoop");
        consume(TokenType.SQUIGGLE_EQUALS);
        consume(TokenType.O_PAREN);
        variableInitialization();
        consume(TokenType.SEMI);
        comparison();
        consume(TokenType.SEMI);
        expression();
        consume(TokenType.C_PAREN);
        block();
    }

    public void whileLoop() {
        log("while Loop");
        consume(TokenType.DIAMOND);
        consume(TokenType.O_PAREN);
        comparison();
        consume(TokenType.C_PAREN);
        block();
    }

    public void infiniteLoop() {
        log("infinite loop");
        consume(TokenType.INFINITY);
        block();
    }

    public void ifStatement() {
        log("if Statement");
        consume(TokenType.RIGHT_ARROW);
        consume(TokenType.O_PAREN);
        comparison();
        consume(TokenType.C_PAREN);
        block();
        if (elseStatementPending()) elseStatement();
        else if (elseStatementPendingNext()) {consume(TokenType.DOUBLE_RIGHT_ARROW); ifStatement(); elseStatement(); }
        else if (ifStatementPending()) ifStatement();
    }

    public void elseStatement() {
        log("else statement");
        consume(TokenType.LEFT_ARROW);
        block();
    }

    public void arrayInitialization() {
        log("array initialization");
        consume(TokenType.A_CIRCLE);
        consume(TokenType.O_PAREN);
        expression();
        consume(TokenType.C_PAREN);
        consume(TokenType.IDENTIFIER);
    }

    public void arrayListInitialization() {
        log("array list initialization");
        consume(TokenType.A_CIRCLE_LIST);
        consume(TokenType.O_PAREN);
        consume(TokenType.C_PAREN);
        consume(TokenType.IDENTIFIER);
    }





    //pending functions


    public boolean programPending() {
        return statementListPending();
    }

    public boolean statementListPending() {
        return statementPending();
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
        return check(TokenType.IDENTIFIER)|| check(TokenType.INTEGER) || check(TokenType.STRING) || check(TokenType.DOUBLE);
    }

    public boolean parameterPending() {
        return  (check(TokenType.IDENTIFIER) && checkNext(TokenType.COMMA)) ||check(TokenType.IDENTIFIER);
    }

    public boolean elseStatementPending() {
        return check(TokenType.LEFT_ARROW);
    }
    public boolean elseStatementPendingNext() {
        return checkNext(TokenType.LEFT_ARROW);
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
        return check(TokenType.TRUE_KEYWORD) || check(TokenType.FALSE_KEYWORD) || check(TokenType.IDENTIFIER) || comparisonPending();
    }

    public boolean printStatementPending() {
        return check(TokenType.PRINT_KEYWORD);
    }

    public boolean functionInputPending() {
        return (primaryPending()&& checkNext(TokenType.COMMA)) || primaryPending();
    }

    private void log(String message) {
        if (printDebugMessages) System.out.println(message);
    }

    private void error(String message) {
        Funalphabet.syntaxError(message, currentLexeme);
    }

















}
