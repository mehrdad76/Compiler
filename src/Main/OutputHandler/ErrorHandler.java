package Main.OutputHandler;

import Main.PriorityRegexDictionary.Tuple;

import java.util.ArrayList;

import static Main.Main.writeToFile;

public class ErrorHandler {
    private static ArrayList<String> errors;

    public static void addLexerError(Tuple<Integer, String> error){
        if (errors == null)
            errors = new ArrayList<>();
        ArrayList<Tuple<Integer, String>> e = new ArrayList<>();
        String text = error.key + ": (" + error.value.trim() + ", invalid input)";
        errors.add(text);
    }

    public static void addParserError(Integer line, String error){
        if (errors == null)
            errors = new ArrayList<>();
        errors.add(line + error);
    }

    public static void writeInFile(){
        StringBuilder s = new StringBuilder();
        if (errors != null)
            for (int i = 0; i < errors.size() ; i++) {
                s.append(errors.get(i));
                s.append("\r\n");
            }
        writeToFile(s.toString(), "errors.txt");
    }

    private ErrorHandler(){}
}
