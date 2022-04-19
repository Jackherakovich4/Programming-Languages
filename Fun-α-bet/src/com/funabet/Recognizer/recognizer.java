package com.funabet.Recognizer;

import com.funabet.Funalphabet;
import com.funabet.lexicalAnalysis.Lexer;
import com.funabet.lexicalAnalysis.Lexeme;
import com.funabet.lexicalAnalysis.TokenType;
import java.util.ArrayList;

public class recognizer {

    private int nextLexemeIndex=0;
    private final boolean printDebugMessages=false;
    private ArrayList<Lexeme> lexemes;
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

        if (statementListPending()) {
            return statementList();
        }
        else return null;
    }

    public Lexeme statementList() {
        log("statementList");
        Lexeme SL = new Lexeme(TokenType.STATEMENT_LIST, -1);
        if (statementPending())  SL.setRight(statement());
        if (statementListPending()) SL.setLeft(statementList());
        return SL;
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
        else if (primaryPending()) return primary();
        else error("expected statement");
        return null;
    }

    public Lexeme functionCall() {
        log ("functionCall");
        Lexeme tree = new Lexeme(TokenType.CLOSURE_CALL, currentLexeme.getLineNumber());
        Lexeme CP = null;
        Lexeme ID = consume(TokenType.IDENTIFIER);
        consume(TokenType.O_PAREN);
        if (functionInputPending()) {  CP = functionInput();}
        consume(TokenType.C_PAREN);
        tree.setLeft(ID);
        tree.setRight(CP);
        return tree;
    }

    public Lexeme function() {
        log("function");
        Lexeme glue = new Lexeme(TokenType.GLUE, -1);
        Lexeme P = null;
        Lexeme CapPi = consume(TokenType.CAPITAL_PI);
        Lexeme TYPE = null;
        if(integerKeywordPending()) {
            TYPE= consume(TokenType.INTEGER_KEYWORD);
        } else if (stringKeywordPending()) {
            TYPE = consume(TokenType.STRING_KEYWORD);
        } else if (boolKeywordPending()) {
            TYPE = consume(TokenType.BOOL_KEYWORD);
        } else if (realKeywordPending()) {
            TYPE= consume(TokenType.REAL_KEYWORD);
        }else {
            error("Function type not defined");
        }
        Lexeme ID = consume(TokenType.IDENTIFIER);
        consume(TokenType.O_PAREN);

        if(parameterPending()) P = parameter();
        Lexeme CP = consume(TokenType.C_PAREN);
        Lexeme B = block();
        CapPi.setRight(glue);
        CapPi.setLeft(ID);
        ID.setLeft(TYPE);
        ID.setRight(CP);
        glue.setLeft(B);
        glue.setRight(P);
        return CapPi;
    }

    public Lexeme parameter() {
        log("parameter");
        Lexeme TYPE=null;
        if(integerKeywordPending()) {
            TYPE= consume(TokenType.INTEGER_KEYWORD);
        } else if (stringKeywordPending()) {
            TYPE = consume(TokenType.STRING_KEYWORD);
        } else if (boolKeywordPending()) {
            TYPE = consume(TokenType.BOOL_KEYWORD);
        } else if (realKeywordPending()) {
            TYPE= consume(TokenType.REAL_KEYWORD);
        }else {
            error("Function type not defined");
        }
        if (check(TokenType.IDENTIFIER)&&checkNext(TokenType.COMMA)) {
            Lexeme ID = consume(TokenType.IDENTIFIER);
            consume(TokenType.COMMA);
            Lexeme P = parameter();
            ID.setRight(TYPE);
            ID.setLeft(P);
            return ID;
        }
        else if (check(TokenType.IDENTIFIER)) {
            Lexeme ID = consume(TokenType.IDENTIFIER);
            ID.setRight(TYPE);
            return ID;
        }
        else error("expected parameter");
        return null;
    }

    public Lexeme block() {
        log("block");
        Lexeme UQ = consume(TokenType.UPSIDEDOWN_QUESTION);
        if (!checkNext(TokenType.QUESTION)) {
        Lexeme SL = statementList();
            Lexeme Q = consume(TokenType.QUESTION);
            UQ.setLeft(SL);
            UQ.setRight(Q);
            return UQ;
        }
        Lexeme Q = consume(TokenType.QUESTION);
        UQ.setRight(Q);
        return UQ;
    }

    public Lexeme functionInput() {
        log("functionInput");
        if (primaryPending() && checkNext(TokenType.COMMA)) {
            Lexeme pri = primary();
            consume(TokenType.COMMA);
            Lexeme FI = functionInput();
            pri.setRight(FI);
            return pri;

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
        else  if (check(TokenType.TRUE_KEYWORD)) return consume(TokenType.TRUE_KEYWORD);
        else if (check(TokenType.FALSE_KEYWORD)) return consume(TokenType.FALSE_KEYWORD);
        else{ error("primary expected"); return null; }
    }

    public Lexeme booleanx(){
        log("boolean");
        if (check(TokenType.FALSE_KEYWORD)) return consume(TokenType.FALSE_KEYWORD);
        else if (check(TokenType.TRUE_KEYWORD)) return consume(TokenType.TRUE_KEYWORD);
        else if (comparisonPending()) return comparison();
        else error("boolean expected");
        return null;
    }

    public Lexeme comparison() {
        log("comparison");
        Lexeme pri1 = primary();
        Lexeme C = comparator();
        Lexeme pri2 = primary();

        C.setLeft(pri1);
        C.setRight(pri2);
        return C;
    }

    public Lexeme comparator() {
        log("Comparator");
        if (check(TokenType.EQUALSCOMPARISON)) return consume(TokenType.EQUALSCOMPARISON);
        else if (check(TokenType.GREATER_THAN_OR_EQUAL)) return consume(TokenType.GREATER_THAN_OR_EQUAL);
        else if (check(TokenType.GREATER_THAN)) return consume(TokenType.GREATER_THAN);
        else if (check(TokenType.LESS_THAN_OR_EQUAL)) return consume(TokenType.LESS_THAN_OR_EQUAL);
        else if (check(TokenType.LESS_THAN)) return consume(TokenType.LESS_THAN);
        else error("comparator expected");
        return null;
    }

    public Lexeme variableInitialization() {
        log("variableInitialization");
        Lexeme glue = new Lexeme(TokenType.GLUE, -1);
        Lexeme R = null;
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
        } else if (realKeywordPending()) {
            R= consume(TokenType.REAL_KEYWORD);
        }else {

            error("type of variable expected");
        }
        Lexeme ID = consume(TokenType.IDENTIFIER);
        Lexeme EA = consume(TokenType.EQUALSASSIGN);
        Lexeme Express = expression();
        UE.setRight(Express);
        UE.setLeft(glue);
        if (I != null) {
            glue.setLeft(I);
        } else if (B!=null) {
            glue.setLeft(B);
        } else if (R!=null){
            glue.setLeft(R);
        } else {
            glue.setLeft(S);
        }
        glue.setRight(ID);
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
        else if (comparisonPending()) {
            Lexeme L = primary();
            Lexeme C = comparator();
            Lexeme R = primary();
            C.setLeft(L);
            C.setRight(R);
            return C;
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
        else error ("binary Operator expected");
        return null;
    }

    public Lexeme unaryOperator() {
        log("unary Operator");
        if (check(TokenType.MINUSMINUS)) return consume(TokenType.MINUSMINUS);
        else if (check(TokenType.PLUSPLUS)) return consume(TokenType.PLUSPLUS);
        else error("unary Operator expected");
        return null;
    }

    public Lexeme assignment() {
        log("assignment");
        Lexeme assign = new Lexeme( TokenType.ASSIGN, currentLexeme.getLineNumber());
        Lexeme ID = consume(TokenType.IDENTIFIER);
        if (check(TokenType.EQUALSASSIGN)) {
            Lexeme EA = consume(TokenType.EQUALSASSIGN);
            Lexeme EX = expression();
            EA.setLeft(ID);
            EA.setRight(EX);
            assign.setRight(EA);
            return assign;
        }
        else if (unaryOperatorPending()) {
            Lexeme UOp = unaryOperator();
            UOp.setLeft(ID);
            assign.setRight(UOp);
            return assign;
        }
        else error("variable assignment expected");
        return null;
    }

    public Lexeme forLoop() {
        log("forLoop");
        Lexeme Glue1 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Glue2 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Glue3 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Glue4 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Glue5 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Glue6 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Bool = null;
        Lexeme C = null;
        Lexeme SE = consume(TokenType.SQUIGGLE_EQUALS);
        Lexeme OP = consume(TokenType.O_PAREN);
        Lexeme Vinit = variableInitialization();
        Lexeme Semi1 = consume(TokenType.SEMI);
        if (comparisonPending()) {
            C = comparison();

        Lexeme Semi2 = consume(TokenType.SEMI);
        Lexeme EX = assignment();
        Lexeme CP = consume(TokenType.C_PAREN);
        Lexeme B = block();

        SE.setLeft(Glue1);
        SE.setRight(Glue2);
        Glue1.setLeft(Glue3);
        Glue1.setRight(Glue4);
        Glue2.setLeft(Glue5);
        Glue2.setRight(Glue6);
        Glue3.setLeft(OP);
        Glue3.setRight(Vinit);
        Glue4.setLeft(Semi1);
        Glue4.setRight(C);

            Glue5.setLeft(Semi2);
            Glue5.setRight(EX);
            Glue6.setLeft(OP);
            Glue6.setRight(B);
            return SE;
    }
        else {
            Bool = booleanx();
        Lexeme Semi2 = consume(TokenType.SEMI);
        Lexeme EX = expression();
        Lexeme CP = consume(TokenType.C_PAREN);
        Lexeme B = block();

        SE.setLeft(Glue1);
        SE.setRight(Glue2);
        Glue1.setLeft(Glue3);
        Glue1.setRight(Glue4);
        Glue2.setLeft(Glue5);
        Glue2.setRight(Glue6);
        Glue3.setLeft(OP);
        Glue3.setRight(Vinit);
        Glue4.setLeft(Semi1);
        Glue4.setRight(Bool);

            Glue5.setLeft(Semi2);
            Glue5.setRight(EX);
            Glue6.setLeft(OP);
            Glue6.setRight(B);
            return SE;
        }

    }

    public Lexeme whileLoop() {
        log("while Loop");
        Lexeme Glue1 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Glue2 = new Lexeme(TokenType.GLUE, -1);
        Lexeme D = consume(TokenType.DIAMOND);
        Lexeme OP = consume(TokenType.O_PAREN);
        Lexeme C = comparison();
        Lexeme CP = consume(TokenType.C_PAREN);
        Lexeme B = block();

        D.setLeft(Glue1);
        D.setRight(Glue2);
        Glue1.setLeft(OP);
        Glue1.setRight(C);
        Glue2.setLeft(CP);
        Glue2.setRight(B);
        return D;
    }

    public Lexeme infiniteLoop() {
        log("infinite loop");
        Lexeme I = consume(TokenType.INFINITY);
        Lexeme B = block();
        I.setRight(B);
        return I;
    }

    public Lexeme ifStatement() {
        log("if Statement");
        Lexeme Glue1 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Glue2 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Glue3 = new Lexeme(TokenType.GLUE, -1);
        Lexeme RA = consume(TokenType.RIGHT_ARROW);
        Lexeme OP = consume(TokenType.O_PAREN);
        Lexeme C = comparison();
        Lexeme CP = consume(TokenType.C_PAREN);
        Lexeme B = block();
        if (elseStatementPending()) {
            Lexeme ES = elseStatement();

            RA.setLeft(Glue1);
            RA.setRight(ES);
            Glue1.setLeft(Glue2);
            Glue1.setRight(Glue3);
            Glue2.setLeft(OP);
            Glue2.setRight(C);
            Glue3.setLeft(CP);
            Glue3.setRight(B);
            return RA;
        } else if (elseIfStatementPending()) {
            Lexeme DRA = consume(TokenType.DOUBLE_RIGHT_ARROW);
            Lexeme IS = ifStatement();

            RA.setRight(DRA);
            DRA.setLeft(IS);
            RA.setLeft(Glue1);
            Glue1.setLeft(Glue2);
            Glue1.setRight(Glue3);
            Glue2.setLeft(OP);
            Glue2.setRight(C);
            Glue3.setLeft(CP);
            Glue3.setRight(B);
            return RA;

        }
        RA.setLeft(Glue1);
        Glue1.setLeft(Glue2);
        Glue1.setRight(Glue3);
        Glue2.setLeft(OP);
        Glue2.setRight(C);
        Glue3.setLeft(CP);
        Glue3.setRight(B);
        return RA;
    }

    public Lexeme elseStatement() {
        log("else statement");
        Lexeme LA = consume(TokenType.LEFT_ARROW);
        Lexeme B = block();
        LA.setRight(B);
        return LA;
    }

    public Lexeme arrayInitialization() {
        log("array initialization");
        Lexeme Glue1 = new Lexeme(TokenType.GLUE, -1);
        Lexeme Glue2 = new Lexeme(TokenType.GLUE, -1);
        Lexeme ACircle = consume(TokenType.A_CIRCLE);
        Lexeme OP = consume(TokenType.O_PAREN);
        Lexeme EX = expression();
        Lexeme CP = consume(TokenType.C_PAREN);
        Lexeme ID = consume(TokenType.IDENTIFIER);

        ACircle.setLeft(Glue1);
        ACircle.setRight(Glue2);
        Glue1.setLeft(OP);
        Glue1.setRight(EX);
        Glue2.setLeft(CP);
        Glue2.setRight(ID);
        return ACircle;
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

    public boolean elseIfStatementPending() {
        return check(TokenType.DOUBLE_RIGHT_ARROW);
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
        return check(TokenType.IDENTIFIER)|| check(TokenType.INTEGER) || check(TokenType.STRING) || check(TokenType.DOUBLE) || check(TokenType.TRUE_KEYWORD) || check(TokenType.FALSE_KEYWORD);
    }

    public boolean parameterPending() {
        return  (check(TokenType.INTEGER_KEYWORD)|| check(TokenType.STRING_KEYWORD) || check(TokenType.BOOL_KEYWORD) || check(TokenType.REAL_KEYWORD) && checkNext(TokenType.IDENTIFIER));
    }

    public boolean elseStatementPending() {
        return check(TokenType.LEFT_ARROW);
    }
    public boolean elseStatementPendingNext() {
        return checkNext(TokenType.LEFT_ARROW);
    }

    public boolean comparisonPending() {
        return (primaryPending() && comparatorPendingNext());
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

    public boolean realKeywordPending() {
        return check(TokenType.REAL_KEYWORD);
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

    public void printTree(Lexeme root, int tabcount) {
        System.out.println(root);
        if (root.getLeft()!=null) {
            for (int i = 0; i<=tabcount; i++) {
            System.out.print("    ");
            }
            System.out.print("with the left child: ");
            printTree(root.getLeft(),tabcount+1);
        }
        if (root.getRight()!=null) {
            for (int i = 0; i<=tabcount; i++) {
                System.out.print("    ");
            }
            System.out.print("with the right child: ");
            printTree(root.getRight(), tabcount+1);
        }
    }

    private void error(String message) {
        Funalphabet.syntaxError(message, currentLexeme);
    }

















}
