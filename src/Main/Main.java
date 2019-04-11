package Main;

import Main.Lexer.Lexer;
import Main.Lexer.State;
import Main.PriorityRegexDictionary.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Lexer lexer = new Lexer();

        Tuple<ArrayList<Tuple<Integer, Tuple<String, String>>>, ArrayList<Tuple<Integer, String>>> a = lexer.analyse("code.c");// be tartib avvali token va dovomi error. too har kodoom avvali shomare khat va dovomi error ya tokene. :)

        for (int i = 0; i < a.key.size(); i++) {
            System.out.println(a.key.get(i).key + "   " + a.key.get(i).value);
        }

        System.out.println();

        for (int i = 0; i < a.value.size(); i++) {
            System.out.println(a.value.get(i));
        }

    }
}
