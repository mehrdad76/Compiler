package Main;

import Main.OutputHandler.ErrorHandler;
import Main.Lexer.Lexer;
import Main.OutputHandler.ParseHandler;
import Main.Parser.TDParser;
import Main.PriorityRegexDictionary.Tuple;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        /////////////////////// phase 1
//        Lexer lexer = new Lexer();
//
//        Tuple<ArrayList<Tuple<Integer, Tuple<String, String>>>, ArrayList<Tuple<Integer, String>>> a = lexer.analyse("sample.c");// be tartib avvali token va dovomi error. too har kodoom avvali shomare khat va dovomi error ya tokene. :)
//
//        List<Tuple<Integer, Tuple<String, String>>> analyzed = a.key;
//        List<Tuple<Integer, String>> errors = a.value;
//
//        writeToFile(analyzedToString(analyzed), "scanner.txt");
//        writeToFile(errorsToString(errors), "errors.txt");


        /////////////////// phase 2

        TDParser tdParser = new TDParser();
        tdParser.createDiagrams("grammar.txt");
        tdParser.createFF("ff.txt");
        try {
            tdParser.parse(new Lexer(), "sample.c", tdParser.diagrams.get(0), tdParser.diagrams.get(0).getStartState(), true, 0);
        } catch (Exception e){
            System.out.println("error!");
        } finally {
            System.out.println("done!");
            ErrorHandler.writeInFile();
            ParseHandler.print();
        }

        /////////////////////     test


    }

    public static void writeToFile(String string, String fileName) {
        try (PrintWriter out = new PrintWriter(fileName)) {
            out.print(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String analyzedToString(List<Tuple<Integer, Tuple<String, String>>> analyzed) {
        int maxLine = -1;
        for (Tuple<Integer, Tuple<String, String>> item : analyzed) {
            if (item.key > maxLine)
                maxLine = item.key;
        }

        if (maxLine == -1)
            return "";

        String[] lines = new String[maxLine + 1];
        for (int i = 0; i < maxLine + 1; i++) {
            lines[i] = "";
        }

        for (Tuple<Integer, Tuple<String, String>> item : analyzed) {
            int lineNumber = item.key;
            String type = item.value.key;
            String token = item.value.value;
            if (!type.equals("WHITESPACE") && !type.equals("COMMENT")) {
                lines[lineNumber] += "(" + type.trim() + ", " + token.trim() + ") ";
            }
        }

        StringBuilder res = new StringBuilder();
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() > 0)
                res.append(i).append(". ").append(lines[i]).append("\n");
        }
        return res.toString();
    }

    public static String errorsToString(List<Tuple<Integer, String>> errors) {
        int maxLine = -1;
        for (Tuple<Integer, String> item : errors) {
            if (item.key > maxLine)
                maxLine = item.key;
        }

        if (maxLine == -1)
            return "";

        String[] lines = new String[maxLine + 1];
        for (int i = 0; i < maxLine + 1; i++) {
            lines[i] = "";
        }

        for (Tuple<Integer, String> item : errors) {
            int lineNumber = item.key;
            String error = item.value;
            lines[lineNumber] += "(" + error.trim() + ", invalid input) ";
        }

        StringBuilder res = new StringBuilder();
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() > 0)
                res.append(i).append(". ").append(lines[i]).append("\n");
        }
        return res.toString();
    }
}
