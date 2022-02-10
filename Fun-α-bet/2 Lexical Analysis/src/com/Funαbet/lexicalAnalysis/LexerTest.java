package com.Funαbet.lexicalAnalysis;

public class LexerTest {

    public static void main(String[] args) {
        Lexeme integerKeyword=new Lexeme(TokenType.INTEGER_KEYWORD, 1);
        System.out.println(integerKeyword);
        Lexeme x = new Lexeme(TokenType.IDENTIFIER, 1, "x");
        System.out.println("");
        Lexeme assignmentOperator = new Lexeme(TokenType.EQUALSASSIGN, 1);
        System.out.println(assignmentOperator);
        Lexeme ten= new Lexeme(TokenType.INTEGER, 1, 10);
        System.out.println(ten);

    }

}
