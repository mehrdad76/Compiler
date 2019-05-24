package Main.OutputHandler;

import Main.Main;

import java.util.ArrayList;

public class ParseHandler {
    private static ArrayList<String> tree = new ArrayList<>();

    public static void add(String s){
        tree.add(s);
    }

    public static void print(){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < tree.size() ; i++) {
            s.append(tree.get(i));
            s.append("\r\n");
        }
        Main.writeToFile(s.toString(), "parseTree.txt");
    }

}
