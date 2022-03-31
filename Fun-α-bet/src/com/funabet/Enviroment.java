package com.funabet;

import com.funabet.lexicalAnalysis.Lexeme;
import com.sun.tools.javac.Main;

import java.util.ArrayList;


public class Enviroment {

    private Enviroment parent;
    private String name;
    private ArrayList<Lexeme> indentifiers;
    private ArrayList<Lexeme> values;

   public Enviroment(Enviroment parent, String name) {
       this.parent=parent;
       this.name=name;
       this.values = new ArrayList<Lexeme>();
       this.indentifiers = new ArrayList<Lexeme>();
   }

   public Enviroment(String name) {
        this.name=name;
       this.values = new ArrayList<Lexeme>();
       this.indentifiers = new ArrayList<Lexeme>();
   }

    public Enviroment getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public void insert(Lexeme ID, Lexeme Val) {
       indentifiers.add(ID);
       values.add(Val);

   }

   public void modify(Lexeme ID, Lexeme replacementVal) {
       int position=0;
       for (Lexeme lu : indentifiers) {
           if (ID==lu) {
               break;
           } else {
               position++;
           }
       }
       values.set(position, replacementVal);
   }

   public Enviroment lookup(Enviroment base, Lexeme lookup) {
       boolean x=false;
       for (Lexeme lu : indentifiers) {
           if (lu==lookup) {
               x=true;
           }
       }
       if (x) {
            return base;
       } else {
           lookup(base.getParent(), lookup);
       }
       return null;
   }

   public void printEnviroment(Enviroment enviroment) {
       System.out.println("------Enviroment------");
       if (enviroment.getParent()==null) {
           System.out.println(enviroment.getName() + " This is a global level enviroment");
       } else {
           System.out.println(enviroment.getName() + " with parent " +enviroment.getParent().getName());
       }
       System.out.println("Identifiers");
       for (int i=0; i<indentifiers.size(); i++) {
           System.out.println(indentifiers.get(i));
       }
       System.out.println("Values");
       for (int i=0; i<values.size(); i++) {
           System.out.println(values.get(i));
       }

   }



}
