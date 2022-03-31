package com.funabet;

import com.funabet.lexicalAnalysis.Lexeme;
import com.funabet.lexicalAnalysis.TokenType;

public class EnviroTest {
    public static void main(String[] args) {
Enviroment test1 = new Enviroment("globa");
        Lexeme x = new Lexeme(TokenType.IDENTIFIER, 1);
        Lexeme y = new Lexeme(TokenType.INTEGER, 1, 3);
test1.insert(x,y);
test1.printEnviroment(test1);
    }

}
