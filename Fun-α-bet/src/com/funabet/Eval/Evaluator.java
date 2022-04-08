package com.funabet.Eval;

import com.funabet.Enviroment;
import com.funabet.lexicalAnalysis.Lexeme;
import com.funabet.lexicalAnalysis.TokenType;

public class Evaluator {

    public Lexeme eval(Lexeme tree, Enviroment enviroment) {
        if (tree == null) {
        return null;}

        switch (tree.getType()) {
            case STATEMENT_LIST:
                return evalStatementlist (tree, enviroment);


            case INTEGER, DOUBLE, STRING, TRUE_KEYWORD, FALSE_KEYWORD:
                return tree;


            case IDENTIFIER :
                return enviroment.lookup(enviroment, tree);

            case EQUALSCOMPARISON, GREATER_THAN, LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN_OR_EQUAL:
                return evalComparison(tree, enviroment);


            case PLUS, MINUS, TIMES, DIVIDE, MODULUS :
                return evalSimpleOperator(tree, enviroment);


            default :
                return null;
        }
    }

    Lexeme evalStatementlist(Lexeme tree, Enviroment enviroment) {
        if (tree == null) return null;
        evalStatementlist(tree.getLeft(), enviroment);
        return eval(tree.getRight(), enviroment);
    }

    TokenType stupidHelperMethodForNegatives(Lexeme tree, Enviroment enviroment) {
        if (tree.getLeft()==null) {
            return TokenType.GLUE;
        } else {
            return tree.getLeft().getType();
        }
    }

    Lexeme evalSimpleOperator(Lexeme tree, Enviroment enviroment) {
        switch (tree.getType()) {
            case PLUS :
                switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.INTEGER, tree.getLineNumber(),tree.getLeft().getNumberval()+tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), tree.getLeft().getRealval()+(double) tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), tree.getLeft().getRealval()+(double) tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), tree.getLeft().getNumberval()+tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                        default :
                            return error("function lol");
                    }
                case STRING, BOOLEAN: return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }
           case MINUS: switch (stupidHelperMethodForNegatives(tree,enviroment)) {
               case INTEGER :
                   switch (eval(tree.getRight(), enviroment).getType()) {
                       case INTEGER :
                           return new Lexeme (TokenType.INTEGER, tree.getLineNumber(),tree.getLeft().getNumberval()-tree.getRight().getNumberval());
                       case DOUBLE :
                           return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), tree.getLeft().getRealval()-(double) tree.getRight().getNumberval());
                       case STRING, BOOLEAN:
                           return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                       default : return error("function lol");
                   }
               case DOUBLE :
                   switch (eval(tree.getRight(), enviroment).getType()) {
                       case INTEGER :
                           return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), tree.getLeft().getRealval()-(double) tree.getRight().getNumberval());
                       case DOUBLE :
                           return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), tree.getLeft().getNumberval()-tree.getRight().getNumberval());
                       case STRING, BOOLEAN :
                           return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                       default :
                           return error("function lol");
                   }
               case STRING, BOOLEAN: return error("Can't combine "+ tree.getRight().getType() +" and Integer");

               default: switch (eval(tree.getRight(), enviroment).getType()) {
                   case INTEGER:
                       return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), -(tree.getRight().getNumberval()));
                   case DOUBLE:
                       return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), -(tree.getRight().getRealval()));
                   case BOOLEAN:
                       return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), !tree.getRight().getBoolval());
                   default: return error("Im throwing simple operator coding");
               }
           }
            case TIMES: switch ((eval(tree.getRight(), enviroment)).getType()) {
                case INTEGER :switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER:
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), tree.getLeft().getNumberval() * tree.getRight().getNumberval());
                    case DOUBLE:
                        return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), tree.getLeft().getRealval() * tree.getRight().getNumberval());
                    case STRING, BOOLEAN:
                        return error("Can't combine " + tree.getRight().getType() + " and Integer");
                    default:
                        return error("function lol");
                }
                case DOUBLE: switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER :
                        return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), tree.getLeft().getRealval()* tree.getRight().getNumberval());
                    case DOUBLE :
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), tree.getLeft().getNumberval()* tree.getRight().getNumberval());
                    case STRING, BOOLEAN :
                        return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                    default :
                        return error("function lol");
                }
                case STRING, BOOLEAN: return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }
            case DIVIDE: switch ((eval(tree.getRight(), enviroment)).getType()) {
                case INTEGER :switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER:
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), tree.getLeft().getNumberval() * tree.getRight().getNumberval());
                    case DOUBLE:
                        return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), tree.getLeft().getRealval() * tree.getRight().getNumberval());
                    case STRING, BOOLEAN:
                        return error("Can't combine " + tree.getRight().getType() + " and Integer");
                    default:
                        return error("function lol");
                }
                case DOUBLE: switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER :
                        return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), tree.getLeft().getRealval()/tree.getRight().getNumberval());
                    case DOUBLE :
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), tree.getLeft().getNumberval()/tree.getRight().getNumberval());
                    case STRING, BOOLEAN :
                        return error("Can't combine "+ tree.getRight().getType() +" and Double");
                    default :
                        return error("function lol");
                }
                case STRING, BOOLEAN: return error("Can't combine "+ tree.getRight().getType() + " and " + tree.getLeft().getType());
                default: return error("Im throwing simple operator coding");
            }
            case MODULUS:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER:
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER:
                            return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), tree.getLeft().getNumberval() % tree.getRight().getNumberval());
                        case DOUBLE, STRING, BOOLEAN:
                            return error("Can't combine " + tree.getRight().getType() + " and Integer");
                        default:
                            return error("function lol");
                    }
                case DOUBLE, STRING, BOOLEAN:
                    return error("Can't combine " + tree.getRight().getType() + " and " + tree.getLeft().getType());
            }

            default: return error("Im throwing simple operator coding");
        }
    }

    Lexeme evalComparison(Lexeme tree, Enviroment enviroment) {
        switch (tree.getType()) {
            case GREATER_THAN:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getNumberval()>tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()>(double) tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()>(double) tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getNumberval()>tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getStringval().length()  > tree.getRight().getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            case GREATER_THAN_OR_EQUAL:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getNumberval()>=tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()>=(double) tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()>=(double) tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getNumberval()>=tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getStringval().length()  >= tree.getRight().getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            case LESS_THAN:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getNumberval()<tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()<(double) tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()<(double) tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getNumberval()<tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getStringval().length()  < tree.getRight().getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            case LESS_THAN_OR_EQUAL:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getNumberval()<=tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()<=(double) tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()<=(double) tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getNumberval()<=tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getStringval().length()  <= tree.getRight().getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            case EQUALSCOMPARISON:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getNumberval()==tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()==(double) tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getRealval()==(double) tree.getRight().getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), tree.getLeft().getNumberval()==tree.getRight().getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING:if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getStringval().length()  == tree.getRight().getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ tree.getRight().getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            default: return error("Invalid Comparison");

        }
    }

    Lexeme error(String message) {
        System.out.println(message);
        return null;
    }
}
