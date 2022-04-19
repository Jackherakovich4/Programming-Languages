package com.funabet.Eval;

import com.funabet.Enviroment;
import com.funabet.lexicalAnalysis.Lexeme;
import com.funabet.lexicalAnalysis.TokenType;

import java.util.ArrayList;

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

            case SQUIGGLE_EQUALS:
                return evalForLoop (tree, enviroment);

            case INTEGER, DOUBLE, STRING:
                return tree;

            case TRUE_KEYWORD:
                return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), true);

            case FALSE_KEYWORD:
                return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), false);

            case RIGHT_ARROW:
                return evalIfStatement(tree,enviroment);

            case DIAMOND:
                return evalWhileLoop(tree, enviroment);

            case INFINITY:
                return evalInfiniteLoop(tree, enviroment);

            case CAPITAL_PI:
                return evalClosure(tree,enviroment);

            case CLOSURE_CALL:
                return evalClosureCall(tree, enviroment);

            case IDENTIFIER :
                if (enviroment.lookup(enviroment, tree)!=null) {
                    return enviroment.lookup(enviroment, tree);
                } else {
                    return new Lexeme(TokenType.STRING, tree.getLineNumber(), tree.getStringval());
                }


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
        Lexeme toReturn = eval(tree.getRight(), enviroment);
        evalStatementlist(tree.getLeft(), enviroment);
        return toReturn;
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
                        case STRING :
                            String x = eval(tree.getRight(), enviroment).getStringval().substring((eval(tree.getRight(), enviroment).getStringval().length()%eval(tree.getLeft(),enviroment).getNumberval()),(eval(tree.getRight(), enviroment).getStringval().length()%eval(tree.getLeft(),enviroment).getNumberval())+1);
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(),eval(tree.getRight(), enviroment).getStringval()+ x );
                        case BOOLEAN :
                            return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                        default : return error("function lol");
                    }
                case DOUBLE :
                    switch (eval(tree.getRight(), enviroment).getType()) {
                        case INTEGER :
                            return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()+(double) eval(tree.getRight(),enviroment).getNumberval());
                        case DOUBLE :
                            return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()+eval(tree.getRight(),enviroment).getNumberval());
                        case STRING:
                            String x = eval(tree.getRight(), enviroment).getStringval().substring((int) (eval(tree.getRight(), enviroment).getStringval().length()%(Math.round( eval(tree.getLeft(),enviroment).getRealval()))),(int)(eval(tree.getRight(), enviroment).getStringval().length()%(Math.round( eval(tree.getLeft(),enviroment).getRealval())))+1);
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(),eval(tree.getRight(), enviroment).getStringval()+ x );
                        case BOOLEAN :
                            return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                        default :
                            return error("function lol");
                    }
                    case STRING: if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.STRING, tree.getLineNumber(),tree.getLeft().getStringval()  + eval(tree.getRight(),enviroment).getStringval());
                    else if (eval(tree.getRight(), enviroment).getType()==TokenType.INTEGER) {
                        String x = eval(tree.getLeft(), enviroment).getStringval().substring((eval(tree.getLeft(), enviroment).getStringval().length()%eval(tree.getRight(),enviroment).getNumberval()),(eval(tree.getLeft(), enviroment).getStringval().length()%eval(tree.getRight(),enviroment).getNumberval())+1);
                        return new Lexeme(TokenType.STRING, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getStringval()+ x );
                    } else if (eval(tree.getRight(), enviroment).getType()==TokenType.DOUBLE) {
                        String x = eval(tree.getLeft(), enviroment).getStringval().substring((int) (eval(tree.getLeft(), enviroment).getStringval().length()%(Math.round( eval(tree.getRight(),enviroment).getRealval()))),(int)(eval(tree.getLeft(), enviroment).getStringval().length()%(Math.round( eval(tree.getRight(),enviroment).getRealval())))+1);
                        return new Lexeme(TokenType.STRING, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getStringval()+ x );
                    }else if (eval(tree.getRight(), enviroment).getType()==TokenType.BOOLEAN) {
                        if (eval(tree.getRight(), enviroment).getBoolval()) {
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getStringval()+ "true" );
                        } else {
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getStringval()+ "false" );
                        }
                    }
                    else return error("can only add strings to strings");
                    case BOOLEAN: if (eval(tree.getRight(), enviroment).getType()==TokenType.STRING) {
                        if (eval(tree.getLeft(), enviroment).getBoolval()) {
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(),"true" + eval(tree.getRight(), enviroment).getStringval() );
                        } else {
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(),"false"+ eval(tree.getRight(), enviroment).getStringval() );
                        }
                    }
                default:
                    System.out.println(eval(tree.getLeft(),enviroment));
                    System.out.println(eval(tree.getRight(),enviroment));
                    return error("Im throwing simple operator coding");
            }
           case MINUS: switch (stupidHelperMethodForNegatives(tree,enviroment)) {
               case INTEGER :
                   switch (eval(tree.getRight(), enviroment).getType()) {
                       case INTEGER :
                           return new Lexeme (TokenType.INTEGER, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getNumberval()-eval(tree.getRight(),enviroment).getNumberval());
                       case DOUBLE :
                           return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()-(double) eval(tree.getRight(),enviroment).getNumberval());
                       case STRING:
                           return new Lexeme (TokenType.INTEGER, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getNumberval()-eval(tree.getRight(),enviroment).getStringval().length());
                       case BOOLEAN:
                           return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                       default : return error("function lol");
                   }
               case DOUBLE :
                   switch (eval(tree.getRight(), enviroment).getType()) {
                       case INTEGER :
                           return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()-(double) eval(tree.getRight(),enviroment).getNumberval());
                       case DOUBLE :
                           return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()-eval(tree.getRight(),enviroment).getNumberval());
                       case STRING:
                           return new Lexeme (TokenType.INTEGER, tree.getLineNumber(),eval(tree.getLeft(), enviroment).getRealval()-eval(tree.getRight(),enviroment).getStringval().length());
                       case BOOLEAN :
                           return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                       default :
                           return error("function lol");
                   }
               case STRING:
                   if (eval(tree.getRight(), enviroment).getType()==TokenType.STRING) {
                       String temp="";
                       int count=0;
                       for (int i = 0; i<eval(tree.getLeft(), enviroment).getStringval().length(); i++) {
                           for (int j = 0; j<eval(tree.getRight(), enviroment).getStringval().length(); j++) {
                               if (eval(tree.getLeft(), enviroment).getStringval().charAt(i)==eval(tree.getRight(), enviroment).getStringval().charAt(j)){
                                    count++;
                               }
                           }
                           if (count==0) {
                               temp+=eval(tree.getLeft(), enviroment).getStringval().substring(i,i+1);
                           }
                           count=0;
                       }
                       return new Lexeme(TokenType.STRING, tree.getLineNumber(), temp);
                   } else if (eval(tree.getRight(), enviroment).getType()==TokenType.BOOLEAN){
                       String temp="";
                        if (eval(tree.getRight(), enviroment).getBoolval()) {
                            for (int i=0; i<(eval(tree.getLeft(), enviroment).getStringval().length()); i++) {
                                if ((eval(tree.getLeft(), enviroment).getStringval().charAt(i)=='t' ||eval(tree.getLeft(), enviroment).getStringval().charAt(i)=='r' || eval(tree.getLeft(), enviroment).getStringval().charAt(i)=='u'|| eval(tree.getLeft(), enviroment).getStringval().charAt(i)=='e')) {
                                } else {
                                    temp+=(eval(tree.getLeft(), enviroment).getStringval().substring(i,i+1));
                                }
                            }
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(), temp);
                        } else {
                            for (int i=0; i<(eval(tree.getLeft(), enviroment).getStringval().length()); i++) {
                                if ((eval(tree.getLeft(), enviroment).getStringval().charAt(i)=='f' ||eval(tree.getLeft(), enviroment).getStringval().charAt(i)=='a' || eval(tree.getLeft(), enviroment).getStringval().charAt(i)=='l'|| eval(tree.getLeft(), enviroment).getStringval().charAt(i)=='s'|| eval(tree.getLeft(), enviroment).getStringval().charAt(i)=='e')) {
                                } else {
                                    temp+=(eval(tree.getLeft(), enviroment).getStringval().substring(i,i+1));
                                }
                            }
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(), temp);
                        }
                   }
               case BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");

               default: switch (eval(tree.getRight(), enviroment).getType()) {
                   case INTEGER:
                       return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), -(eval(tree.getRight(),enviroment).getNumberval()));
                   case DOUBLE:
                       return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), -(eval(tree.getRight(),enviroment).getRealval()));
                   case BOOLEAN:
                       return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), !eval(tree.getRight(),enviroment).getBoolval());
                   case STRING:
                       String temp="";
                       for (int i =eval(tree.getRight(),enviroment).getStringval().length();i>0;i--) {
                           temp+=eval(tree.getRight(),enviroment).getStringval().substring(i-1,i);
                       }
                       return new Lexeme(TokenType.STRING, tree.getLineNumber(), temp);
                   default: return error("Im throwing simple operator coding");
               }
           }
            case TIMES: switch ((eval(tree.getRight(), enviroment)).getType()) {
                case INTEGER :switch (eval(tree.getLeft(), enviroment).getType()) {
                    case INTEGER:
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval() * eval(tree.getRight(),enviroment).getNumberval());
                    case DOUBLE:
                        return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval() * eval(tree.getRight(),enviroment).getNumberval());
                    case STRING:
                        String temp="";
                        for (int i=eval(tree.getRight(), enviroment).getNumberval(); i>0; i--) {
                            temp+=eval(tree.getLeft(), enviroment).getStringval();
                        }
                        return new Lexeme(TokenType.STRING, tree.getLineNumber(), temp);
                    case BOOLEAN:
                        return error("Can't combine " + eval(tree.getLeft(),enviroment).getType() + " and Integer");
                    default:
                        return error("function lol");
                }
                case DOUBLE: switch (eval(tree.getLeft(), enviroment).getType()) {
                    case INTEGER :
                        return new Lexeme (TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval()* eval(tree.getRight(),enviroment).getNumberval());
                    case DOUBLE :
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval()* eval(tree.getRight(),enviroment).getNumberval());
                    case STRING:
                        String temp="";
                        for (double i= (eval(tree.getRight(), enviroment).getRealval())-1; i>=0; i--) {
                            temp+=eval(tree.getLeft(), enviroment).getStringval();
                        }
                        return new Lexeme(TokenType.STRING, tree.getLineNumber(), temp);
                    case
                            BOOLEAN :
                        return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                    default :
                        return error("function lol");
                }
                case STRING:
                    switch (eval(tree.getLeft(), enviroment).getType()) {
                        case INTEGER:
                            String temp="";
                            for (int i=eval(tree.getLeft(), enviroment).getNumberval(); i>0; i--) {
                                temp+=eval(tree.getRight(), enviroment).getStringval();
                            }
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(), temp);
                        case DOUBLE:
                            String temp2="";
                            for (double i= (eval(tree.getLeft(), enviroment).getRealval())-1; i>=0; i--) {
                                temp2+=eval(tree.getRight(), enviroment).getStringval();
                            }
                            return new Lexeme(TokenType.STRING, tree.getLineNumber(), temp2);
                        default: return error("cannot combine String and " + eval(tree.getLeft(), enviroment).getType() );
                    }
                case BOOLEAN:
                    if (eval(tree.getLeft(), enviroment).getType()==TokenType.BOOLEAN) {
                        return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), (eval(tree.getLeft(), enviroment).getBoolval()) && eval(tree.getRight(), enviroment).getBoolval());
                    } else {
                        return error("cant combine boolean with non boolean");
                    }
                default: return error("Im throwing simple operator coding");
            }
            case DIVIDE: switch ((eval(tree.getLeft(), enviroment)).getType()) {
                case INTEGER :switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER:
                        return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getNumberval() / eval(tree.getRight(),enviroment).getNumberval());
                    case DOUBLE:
                        return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getRealval() / eval(tree.getRight(),enviroment).getNumberval());
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
                case STRING: switch (eval(tree.getRight(), enviroment).getType()) {
                    case INTEGER:
                        return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getStringval().substring(0,eval(tree.getLeft(), enviroment).getStringval().length()/(eval(tree.getRight(),enviroment).getNumberval())));
                    case DOUBLE:
                        double temp = (eval(tree.getRight(),enviroment).getRealval());
                        int temp1 = (int) temp;
                        return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getLeft(), enviroment).getStringval().substring(0,eval(tree.getLeft(), enviroment).getStringval().length()/temp1));
                    default:
                        return error("Can only divide string by numbers");
                }
                case BOOLEAN: if (eval(tree.getRight(), enviroment).getType()==TokenType.BOOLEAN) {
                    return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), (!(eval(tree.getLeft(), enviroment).getBoolval())) && !(eval(tree.getRight(), enviroment).getBoolval()));
                } else {
                    return error("cant combine boolean with non boolean");
                } default: return error("Im throwing simple operator coding");
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
                case  STRING:
                    if (eval(tree.getRight(), enviroment).getType()==TokenType.STRING) {
                        return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), (1.0*eval(tree.getLeft(),enviroment).getStringval().length())/(1.0*eval(tree.getRight(),enviroment).getStringval().length()));
                    }
                default:
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
                case STRING:if(eval(tree.getRight(), enviroment).getType()==TokenType.STRING)return new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(),tree.getLeft().getStringval().equals( eval(tree.getRight(),enviroment).getStringval()));
                else return error("can only compare strings to strings");
                case BOOLEAN: return error("Can't combine "+ eval(tree.getRight(),enviroment).getType() +" and Integer");
                default: return error("Im throwing simple operator coding");
            }

            default: return error("Invalid Comparison");

        }
    }

    Lexeme evalVarInit(Lexeme tree, Enviroment enviroment) {
        switch (tree.getLeft().getLeft().getType()) {
            case INTEGER_KEYWORD: enviroment.insert(new Lexeme(TokenType.IDENTIFIER, tree.getLineNumber(), tree.getLeft().getRight().getStringval()), new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getRight(),enviroment).getNumberval())); return enviroment.lookup(enviroment, tree.getLeft().getRight());
            case STRING_KEYWORD: enviroment.insert(new Lexeme(TokenType.IDENTIFIER, tree.getLineNumber(), tree.getLeft().getRight().getStringval()), new Lexeme(TokenType.STRING, tree.getLineNumber(), eval(tree.getRight(),enviroment).getStringval())); return enviroment.lookup(enviroment, tree.getLeft().getRight());
            case BOOL_KEYWORD: enviroment.insert(new Lexeme(TokenType.IDENTIFIER, tree.getLineNumber(), tree.getLeft().getRight().getStringval()), new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getRight(),enviroment).getBoolval())); return enviroment.lookup(enviroment, tree.getLeft().getRight());
            case REAL_KEYWORD: enviroment.insert(new Lexeme(TokenType.IDENTIFIER, tree.getLineNumber(), tree.getLeft().getRight().getStringval()), new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getRight(),enviroment).getRealval())); return enviroment.lookup(enviroment, tree.getLeft().getRight());
            default:return error("Invalid variable Initialization");
        }

    }

    Lexeme evalVarAssign (Lexeme tree, Enviroment enviroment) {
        //TODO
        switch (tree.getRight().getType()) {
            case PLUSPLUS: switch (eval(tree.getRight().getRight(), enviroment).getType()) {
                case INTEGER:enviroment.modify(tree.getRight().getLeft(), new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getRight().getLeft(),enviroment).getNumberval()+1)); System.out.println("hi"); return enviroment.lookup(enviroment, tree.getRight().getLeft());
                case DOUBLE:enviroment.modify(tree.getRight().getLeft(), new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getRealval()+1)); return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getRealval()+1);
                default: return error("can't use unary op on that");
            }
            case MINUSMINUS:switch (eval(tree.getRight().getRight(), enviroment).getType()) {
                case INTEGER:enviroment.modify(eval(tree.getRight().getLeft(),enviroment), new Lexeme(TokenType.INTEGER, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getNumberval()-1)); return new Lexeme(TokenType.INTEGER, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getNumberval()-1);
                case DOUBLE:enviroment.modify(eval(tree.getRight().getLeft(),enviroment), new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getRealval()-1)); return new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), enviroment.lookup(enviroment,eval(tree.getRight().getLeft(),enviroment)).getRealval()-1);
                default: return error("can't use unary op on that");
            }
            case EQUALSASSIGN: switch (eval(tree.getRight().getRight(), enviroment).getType()) {
                case INTEGER: enviroment.modify(tree.getRight().getLeft(), new Lexeme(TokenType.INTEGER, tree.getLineNumber(), eval(tree.getRight().getRight(),enviroment).getNumberval())); return enviroment.lookup(enviroment, tree.getRight().getLeft());
                case DOUBLE: enviroment.modify(tree.getRight().getLeft(), new Lexeme(TokenType.DOUBLE, tree.getLineNumber(), eval(tree.getRight().getRight(),enviroment).getRealval())); return enviroment.lookup(enviroment, tree.getRight().getLeft());
                case STRING: enviroment.modify(tree.getRight().getLeft(), new Lexeme(TokenType.STRING, tree.getLineNumber(), eval(tree.getRight().getRight(),enviroment).getStringval())); return enviroment.lookup(enviroment, tree.getRight().getLeft());
                case BOOLEAN: enviroment.modify(tree.getRight().getLeft(), new Lexeme(TokenType.BOOLEAN, tree.getLineNumber(), eval(tree.getRight().getRight(),enviroment).getBoolval())); return enviroment.lookup(enviroment, tree.getRight().getLeft());
                default: return error("function lol");
            }
            default:return error("Invalid variable assignment");
        }
    }

    Lexeme evalIfStatement(Lexeme tree, Enviroment enviroment) {
        if (eval(tree.getLeft().getLeft().getRight(),enviroment).getBoolval()==true) {
            return eval(tree.getLeft().getRight().getRight().getLeft(), enviroment);
        } else if (eval(tree.getLeft().getLeft().getRight(),enviroment).getBoolval()==false) {
            if (tree.getRight().getType() == TokenType.DOUBLE_RIGHT_ARROW) {
                return evalIfStatement(tree.getRight().getLeft(), enviroment);
            } else if (tree.getRight().getType() == TokenType.LEFT_ARROW) {
                return evalElseStatement(tree.getRight(), enviroment);
            }
        } else {
            return error("if statement must contain boolean in argument");
        }
        return null;
    }

    Lexeme evalElseStatement(Lexeme tree, Enviroment enviroment) {
        return eval(tree.getRight().getLeft(), enviroment);
    }

    Lexeme evalForLoop(Lexeme tree, Enviroment enviroment) {
        Enviroment forLoop = new Enviroment(enviroment, "for");
        evalVarInit(tree.getLeft().getLeft().getRight(), forLoop);

        while (eval(tree.getLeft().getRight().getRight(), forLoop).getBoolval()) {
            evalVarAssign (tree.getRight().getLeft().getRight() , forLoop);
             eval (tree.getRight().getRight().getRight().getLeft(), forLoop);
        }
        return eval(tree.getLeft().getRight().getRight(), forLoop);
    }

    Lexeme evalWhileLoop(Lexeme tree, Enviroment enviroment){
        Enviroment whileLoop = new Enviroment(enviroment, "while");
        while (eval(tree.getLeft().getRight(), whileLoop).getBoolval()) {
            eval (tree.getRight().getRight().getLeft(), whileLoop);
        }
        return eval(tree.getLeft().getRight().getRight(), whileLoop);
    }

    Lexeme evalInfiniteLoop (Lexeme tree, Enviroment enviroment) {

        while (true) {
            eval(tree.getRight().getLeft(), enviroment);
        }

    }

    Lexeme evalClosure (Lexeme tree, Enviroment enviroment) {

        ArrayList<Lexeme> paramList = new ArrayList<Lexeme>();
        if (tree!=null) {
            paramList.add(tree.getRight().getRight());
        }
        ArrayList<Lexeme> temp = evalParamlist(tree.getRight().getLeft() , enviroment, paramList );
        enviroment.insert(tree.getLeft(), new Lexeme(TokenType.CLOSURE, temp, tree.getRight().getLeft().getLeft()));
        return enviroment.lookup(enviroment, tree.getLeft());
    }

    ArrayList<Lexeme> evalParamlist(Lexeme tree, Enviroment enviroment, ArrayList<Lexeme> returnList) {
        returnList.add(tree);
        if (tree.getLeft()!=null) {
            evalParamlist(tree.getLeft(), enviroment , returnList);
        }
        return returnList;
    }

    Lexeme evalClosureCall (Lexeme tree, Enviroment enviroment) {
        Enviroment function = new Enviroment(enviroment, "func");
        Lexeme functionToRun = enviroment.lookup(enviroment, tree.getLeft());
        if (functionToRun==null) {
            return error("function referenced not found");
        }
        int count=0;
        for (Lexeme parameter: functionToRun.getParams()) {
            Lexeme val = tree.getRight();
            for (int i = 0; i<count; i++) {
                if (val==null) {
                    return error("not enough parameters given");
                }
                val=val.getRight();
            }
            function.insert(parameter, val);
            count++;
        }
        return eval(functionToRun.getStatementList(), enviroment);
    }

    Lexeme error(String message) {
        System.out.println(message);
        return null;
    }
}
