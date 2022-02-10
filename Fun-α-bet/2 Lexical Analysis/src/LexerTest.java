public class LexerTest {

    public static void main(String[] args) {
        Lexeme integerKeyword=new Lexeme(TokenType.INTEGER_KEYWORD, 1);
        System.out.println(integerKeyword);
        Lexeme x = new Lexeme(TokenType.IDENTIFIER, 1, "x");


    }

}
