package com.funabet;

import com.funabet.lexicalAnalysis.Lexeme;

import java.util.ArrayList;


public class Enviroment {

    private Enviroment parent;
    private String name;
    private  ArrayList<String> indentifiers;
    private ArrayList<Lexeme> values;

   public Enviroment(Enviroment parent, String name) {
       this.parent=parent;
       this.name=name;
   }

   public Enviroment(String name) {
        this.name=name;
   }

    public Enviroment getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    private void insert(Lexeme ID) {
       indentifiers.add(ID.getStringval());
       values.add(ID);

   }

   private void modify(Lexeme ID, Lexeme replacement) {
       int position=0;
       for (String lu : indentifiers) {
           if (ID.getStringval()==lu) {
               break;
           } else {
               position++;
           }
       }
       indentifiers.set(position, replacement.getStringval());
       values.set(position, replacement);
   }

   private Enviroment lookup(Enviroment base, Lexeme lookup) {
       boolean x=false;
       for (String lu : indentifiers) {
           if (lu==lookup.getStringval()) {
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

   private void printEnviroment(Enviroment enviroment) {
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
