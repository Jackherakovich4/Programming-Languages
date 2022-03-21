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

    private Lexeme consume(TokenType expected) {
        if(check(expected));
        else error("Expected: " + expected + "but found " + currentLexeme + "instead.");
        return advance();
    }

    public recognizer(ArrayList<Lexeme> lexemes) {
        this.lexemes = lexemes;
        this.advance();
    }

    public Lexeme advance() {
        Lexeme x=currentLexeme;
        currentLexeme = lexemes.get(nextLexemeIndex);
        nextLexemeIndex++;
        return x;
    }

    public boolean check(TokenType expected) {
        return currentLexeme.getType() == expected;
    }

    public boolean checkNext(TokenType expected) {
        if (lexemes.size()<=nextLexemeIndex) return false;
        return lexemes.get(nextLexemeIndex).getType() == expected;
    }

    //consumption functs

    public Lexeme program() {
        log("program");
        if (statementListPending()) return statementList();
        else return null;
    }

    public Lexeme statementList() {
        log("statementList");
        if (statementPending()) return statement();
        if (statementListPending()) return statementList();
    }

    public Lexeme statement() {
        log("statement");
        if (functionPending()) return function();
        else if (functionCallPending()) return functionCall();
        else if (variableInitializationPending()) return variableInitialization();
        else if (assignmentPending()) return assignment();
        else if (forLoopPending()) return forLoop();
        else if (whileLoopPending()) return whileLoop();
        else if (infiniteLoopPending()) return infiniteLoop();
        else if (expressionPending()) return expression();
        else if (ifStatementPending()) return ifStatement();
        else if (arrayInitializationPending()) return arrayInitialization();
        else error("expected statement");
        return null;
    }

    public Lexeme functionCall() {
        log ("functionCall");
        Lexeme F = null;
        Lexeme ID = consume(TokenType.IDENTIFIER);
        Lexeme OP = consume(TokenType.O_PAREN);
        if (functionInputPending()) {  F = functionInput();}
        Lexeme CP = consume(TokenType.C_PAREN);
        assert OP !=null;
        OP.setLeft(ID);
        OP.setRight(CP);
        CP.setLeft(F);
        return OP;
    }

    public Lexeme function() {
        log("function");
        Lexeme glue = null;
        Lexeme P = null;
        Lexeme CapPi = consume(TokenType.CAPITAL_PI);
        Lexeme ID = consume(TokenType.IDENTIFIER);
        Lexeme OP = consume(TokenType.O_PAREN);
        if(parameterPending()) P = parameter();
        Lexeme CP = consume(TokenType.C_PAREN);
        Lexeme B = block();
        CapPi.setRight(glue);
        CapPi.setLeft(ID);
        ID.setLeft(OP);
        ID.setRight(CP);
        glue.setRight(B);
        glue.setRight(P);
        return CapPi;
    }

    public Lexeme parameter() {
        log("parameter");
        if (functionPending()) return function();
        else if (check(TokenType.IDENTIFIER)&&check(TokenType.COMMA)) {
            Lexeme ID = consume(TokenType.IDENTIFIER);
            Lexeme C = consume(TokenType.COMMA); parameter();
            ID.setRight(C);
            return ID;
        }
        else if (check(TokenType.IDENTIFIER)) return consume(TokenType.IDENTIFIER);
        else error("expected parameter");
        return null;
    }

    public Lexeme block() {
        log("block");
        Lexeme UQ = consume(TokenType.UPSIDEDOWN_QUESTION);
        Lexeme SL = statementList();
        Lexeme Q = consume(TokenType.QUESTION);
        UQ.setLeft(SL);
        UQ.setRight(Q);
        return UQ;
    }

    public Lexeme functionInput() {
        log("functionInput");
        if (primaryPending() && checkNext(TokenType.COMMA)) {
            Lexeme pri = primary();
            Lexeme comma = consume(TokenType.COMMA);
            Lexeme FI = functionInput();

            comma.setLeft(pri);
            comma.setRight(FI);
            return comma;

        }
        else if (primaryPending()) {Lexeme pri = primary(); return pri; }
        else { error ("expected function input"); return null;}
    }

    public Lexeme primary() {
        log("primary");
        if (check(TokenType.INTEGER)) return consume(TokenType.INTEGER);
        else if (check(TokenType.DOUBLE)) return consume(TokenType.DOUBLE);
        else if (check(TokenType.STRING)) return consume(TokenType.STRING);
        else if (check(TokenType.IDENTIFIER)) return consume(TokenType.IDENTIFIER);
        else{ error("primary expected"); return null; }
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

    public Lexeme variableInitialization() {
        log("variableInitialization");
        Lexeme G = null;
        Lexeme I = null;
        Lexeme S = null;
        Lexeme B = null;
        Lexeme UE = consume(TokenType.UPSIDEDOWN_EXCLAMATION);
        if(integerKeywordPending()) {
            I= consume(TokenType.INTEGER_KEYWORD);
        } else if (stringKeywordPending()) {
            S = consume(TokenType.STRING_KEYWORD);
        } else if (boolKeywordPending()) {
            B = consume(TokenType.BOOL_KEYWORD);
        } else {
            error("type of variable expected");
        }
        Lexeme ID = consume(TokenType.IDENTIFIER);
        Lexeme EA = consume(TokenType.EQUALSASSIGN);
        Lexeme Express = expression();
        UE.setRight(Express);
        UE.setLeft(G);
        if (I != null) {
            G.setLeft(I);
        } else if (B!=null) {
            G.setLeft(B);
        } else {
            G.setLeft(S);
        }
        G.setRight(ID);
        return UE;
    }

    public Lexeme expression() {
        log("expression");
        if(check(TokenType.MINUS)) {
            Lexeme M = consume(TokenType.MINUS);
            Lexeme P = primary();
            M.setRight(P);
            return M;
        }
        else if (primaryPending()) {
            if (checkNext(TokenType.PLUS)||checkNext(TokenType.MINUS)||checkNext(TokenType.TIMES)||checkNext(TokenType.DIVIDE)||checkNext(TokenType.MODULUS)) {
                Lexeme P = primary();
                Lexeme B = binaryOperator();
                Lexeme E = expression();
                B.setRight(E);
                B.setLeft(P);
                return B;
            } else if (checkNext(TokenType.PLUSPLUS)||checkNext(TokenType.MINUSMINUS)) {
                Lexeme P = primary();
                Lexeme U = unaryOperator();
                U.setRight(P);
                return U;
            } else {
                return primary();
            }
        }
        else error("expression expected");
        return null;
    }

    public Lexeme binaryOperator() {
        log("binary operator");
        if (check(TokenType.MINUS)) return consume(TokenType.MINUS);
        else if (check(TokenType.PLUS)) return consume(TokenType.PLUS);
        else if (check(TokenType.TIMES)) return consume(TokenType.TIMES);
        else if (check(TokenType.DIVIDE)) return consume(TokenType.DIVIDE);
        else if (check(TokenType.MODULUS)) return consume(TokenType.MODULUS);
        else error("binary Operator expected");
        return null;
    }

    public Lexeme unaryOperator() {
        log("unary Operator");
        if (check(TokenType.MINUSMINUS)) return consume(TokenType.MINUSMINUS);
        else if (check(TokenType.PLUSPLUS)) return consume(TokenType.PLUSPLUS);
        else error("unary Operator expected");
        return null;
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
        if(comparisonPending()) comparison();
        else booleanx();
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

    public boolean integerKeywordPending() {
        return check(TokenType.INTEGER_KEYWORD);
    }

    public boolean stringKeywordPending() {
        return check(TokenType.STRING_KEYWORD);
    }

    public boolean boolKeywordPending() {
        return check(TokenType.BOOL_KEYWORD);
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
