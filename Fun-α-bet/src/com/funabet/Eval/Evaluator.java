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

            case UPSIDEDOWN_EXCLAMATION:
                return evalVarInit  (tree, enviroment);

            case ASSIGN:
                return evalVarAssign (tree, enviroment);


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
                            return new Lexeme (TokenType.INTEGER, tree.getLineNumber(),eval(tree.getLeft(),enviroment).getNumberval()+eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(),enviroment).getRealval()+(double) eval(tree.getRight(), enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()+(double) eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()+eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                        default :
                            return error("function lol");
                    }
                    case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getStringval().length()  + eval(tree.getRight(),enviroment).getStringval().length());
                    else return error("can only add strings to strings");
                    case BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }
           case MINUS: switch (stupidHelperMethodForNegatives(tree,enviroment)) {
               case INTEGER :
                   switch (eval(tree.getRight(), enviroment).getType()) {
                       case INTEGER :
                           return new Lexeme (TokenType.INTEGER, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getNumberval()-eval(tree.getRight(),enviroment).getNumberval());
                       case DOUBLE :
                           return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()-(double) eval(tree.getRight(),enviroment).getNumberval());
                       case STRING, BOOLEAN:
                           return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                       default : return error("function lol");
                   }
               case DOUBLE :
                   switch (eval(tree.getRight(), enviroment).getType()) {
                       case INTEGER :
                           return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()-(double) eval(tree.getRight(),enviroment).getNumberval());
                       case DOUBLE :
                           return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()-eval(tree.getRight(),enviroment).getNumberval());
                       case STRING, BOOLEAN :
                           return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                       default :
                           return error("function lol");
                   }
               case STRING, BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");

               default: switch (eval(tree.getRight(), enviroment).getType()) {
                   case INTEGER:
                       return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), -(eval(tree.getRight(),enviroment).getNumberval()));
                   case DOUBLE:
                       return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), -(eval(tree.getRight(),enviroment).getRealval()));
                   case BOOLEAN:
                       return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), !eval(tree.getRight(),enviroment).getBoolval());
                   default: return error("Im throwing simple operator coding");
               }
           }
            case TIMES: switch ((eval(tree.getRight(), enviroment)).getType()) {
                case INTEGER :switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER:
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval() * eval(tree.getRight(),enviroment).getNumberval());
                    case DOUBLE:
                        return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval() * eval(tree.getRight(),enviroment).getNumberval());
                    case STRING, BOOLEAN:
                        return error("Can't combine " + eval(tree.getRight(),enviroment).getType() + " and Integer");
                    default:
                        return error("function lol");
                }
                case DOUBLE: switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER :
                        return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()* eval(tree.getRight(),enviroment).getNumberval());
                    case DOUBLE :
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()* eval(tree.getRight(),enviroment).getNumberval());
                    case STRING, BOOLEAN :
                        return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                    default :
                        return error("function lol");
                }
                case STRING, BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }
            case DIVIDE: switch ((eval(tree.getRight(), enviroment)).getType()) {
                case INTEGER :switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER:
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval() * eval(tree.getRight(),enviroment).getNumberval());
                    case DOUBLE:
                        return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval() * eval(tree.getRight(),enviroment).getNumberval());
                    case STRING, BOOLEAN:
                        return error("Can't combine " + eval(tree.getRight(),enviroment).getType() + " and Integer");
                    default:
                        return error("function lol");
                }
                case DOUBLE: switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER :
                        return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()/eval(tree.getRight(),enviroment).getNumberval());
                    case DOUBLE :
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()/eval(tree.getRight(),enviroment).getNumberval());
                    case STRING, BOOLEAN :
                        return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Double");
                    default :
                        return error("function lol");
                }
                case STRING, BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() + " and " + eval(tree.getLeft(), enviroment).getType());
                default: return error("Im throwing simple operator coding");
            }
            case MODULUS:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER:
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER:
                            return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval() % eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE, STRING, BOOLEAN:
                            return error("Can't combine " + eval(tree.getRight(),enviroment).getType() + " and Integer");
                        default:
                            return error("function lol");
                    }
                case DOUBLE, STRING, BOOLEAN:
                    return error("Can't combine " + eval(tree.getRight(),enviroment).getType() + " and " + eval(tree.getLeft(), enviroment).getType());
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
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getNumberval()>eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()>(double) eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ eval(tree.getRight(),enviroment) +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()>(double) eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()>eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getStringval().length()  > eval(tree.getRight(),enviroment).getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment) +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            case GREATER_THAN_OR_EQUAL:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getNumberval()>=eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()>=(double) eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ eval(tree.getRight(),enviroment) +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()>=(double) eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()>= eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getStringval().length()  >= eval(tree.getRight(),enviroment).getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment) +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            case LESS_THAN:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getNumberval()<eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()<(double) eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()<(double) eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()<eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ eval(tree.getRight(),enviroment).getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getStringval().length()  < eval(tree.getRight(),enviroment).getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            case LESS_THAN_OR_EQUAL:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getNumberval()<=eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()<=(double) eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()<=(double) eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()<=eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ eval(tree.getRight(),enviroment).getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getStringval().length()  <= eval(tree.getRight(),enviroment).getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            case EQUALSCOMPARISON:switch (eval(tree.getLeft(),enviroment).getType()) {
                case INTEGER :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getNumberval()==eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()==(double) eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()==(double) eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()==eval(tree.getRight(),enviroment).getNumberval());
                        case STRING, BOOLEAN :
                            return error("Can't compare "+ tree.getRight().getType() +" and Double");
                        default :
                            return error("function lol");
                    }
                case STRING:if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getStringval().length()  == eval(tree.getRight(),enviroment).getStringval().length());
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            default: return error("Invalid Comparison");

        }
    }

    Lexeme evalVarInit(Lexeme tree, Enviroment enviroment) {
        switch (tree.getLeft().getLeft().getType()) {
            case INTEGER_KEYWORD: enviroment.insert(new Lexeme(TokenType.STRING, tree.getLineNumber(), eval(tree.getLeft().getRight(),enviroment).getStringval()), new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getRight(),enviroment).getNumberval())); return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getRight(),enviroment).getNumberval());
            case STRING_KEYWORD: enviroment.insert(new Lexeme(TokenType.STRING, tree.getLineNumber(), eval(tree.getLeft().getRight(),enviroment).getStringval()), new Lexeme(TokenType.STRING, tree.getLineNumber(), eval(tree.getRight(),enviroment).getStringval())); return new Lexeme(TokenType.STRING, tree.getLineNumber(), eval(tree.getRight(),enviroment).getStringval());
            case BOOL_KEYWORD: enviroment.insert(new Lexeme(TokenType.STRING, tree.getLineNumber(), eval(tree.getLeft().getRight(),enviroment).getStringval()), new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getRight(),enviroment).getBoolval())); return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getRight(),enviroment).getBoolval());
            case REAL_KEYWORD: enviroment.insert(new Lexeme(TokenType.STRING, tree.getLineNumber(), eval(tree.getLeft().getRight(),enviroment).getStringval()), new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getRight(),enviroment).getRealval())); return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getRight(),enviroment).getRealval());
            default:return error("Invalid variable Initialization");
        }

    }

    Lexeme evalVarAssign (Lexeme tree, Enviroment enviroment) {
        switch (tree.getRight().getType()) {
            case PLUSPLUS: switch (eval(tree.getRight().getRight(), enviroment).getType()) {
                case INTEGER:enviroment.modify(eval(tree.getRight().getLeft(),enviroment), new Lexeme(TokenType.INTEGER, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getNumberval()+1)); return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getNumberval()+1);
                case DOUBLE:enviroment.modify(eval(tree.getRight().getLeft(),enviroment), new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getRealval()+1)); return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getRealval()+1);
                default: return error("can't use unary op on that");
            }
            case MINUSMINUS:switch (eval(tree.getRight().getRight(), enviroment).getType()) {
                case INTEGER:enviroment.modify(eval(tree.getRight().getLeft(),enviroment), new Lexeme(TokenType.INTEGER, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getNumberval()-1)); return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getNumberval()-1);
                case DOUBLE:enviroment.modify(eval(tree.getRight().getLeft(),enviroment), new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getRealval()-1)); return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getRealval()-1);
                default: return error("can't use unary op on that");
            }
            case EQUALSASSIGN: switch (eval(tree.getRight().getRight(), enviroment).getType()) {
                case INTEGER: enviroment.modify(eval(tree.getRight().getLeft(),enviroment), new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getRight().getLeft(),enviroment).getNumberval())); return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getRight().getRight(),enviroment).getNumberval());
                case DOUBLE: enviroment.modify(eval(tree.getRight().getLeft(),enviroment), new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getRight().getLeft(),enviroment).getNumberval())); return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getRight().getRight(),enviroment).getNumberval());
                case STRING:
                case BOOLEAN:
                default: return error("function lol");
            }
            default:

        }
    }


    Lexeme error(String message) {
        System.out.println(message);
        return null;
    }
}
