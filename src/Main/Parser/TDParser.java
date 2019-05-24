package Main.Parser;

import Main.OutputHandler.ErrorHandler;
import Main.Lexer.Lexer;
import Main.OutputHandler.ParseHandler;
import Main.PriorityRegexDictionary.Tuple;

import java.io.FileInputStream;
import java.util.ArrayList;

public class TDParser {

    public ArrayList<Diagram> diagrams = new ArrayList<>();
    private boolean isNext = true;

    private Tuple<Integer, Tuple<String, String>> res; // use http://smlweb.cpsc.ucalgary.ca/vital-stats.php for first and follow

    public void parse(Lexer lexer, String codeAddress, Diagram currentDiagram, State currentState, boolean isFirstStart, int layer) throws Exception {

        if (isFirstStart){
            res = lexer.get_next_token(codeAddress);
        }

        do {
            Tuple<String, String> aux = getNextTransaction(currentDiagram, currentState, res.value);

            if (currentState.getSName().equals("end"))
                return;

            if (isNext)
                printLayer(layer, currentState.toString());

            isNext = true;

            if (aux == null){
                handleError(currentState, codeAddress, lexer);
            }

            assert aux != null;
            String tr = aux.key;
            String seen = aux.value;

            if (tr.equals("")) {
                currentState = currentState.getNextState(tr);
                printLayer(layer+1, "epsilon");
            } else if(!Character.isUpperCase(tr.charAt(0))){
                currentState = currentState.getNextState(tr);
                if (tr.equals("id")){
                    printLayer(layer+1, "Id");
                    printLayer(layer+2, seen);
                } else {
                    printLayer(layer+1, seen);
                }
                if (res.value.key.equals("EOF"))
                    break;
                res = lexer.get_next_token(codeAddress);
                isNext = false;
            } else {
                Diagram diagram = getDiagram(tr);
                assert diagram != null;
                parse(lexer, codeAddress, diagram, diagram.getStartState(), false, layer + 1);
                currentState = currentState.getNextState(tr);
                isNext = false;
            }


        } while (true);

    }

    private void handleError(State currentState, String codeAddress, Lexer lexer) throws Exception{
        if (currentState.getNextStates().get(0).key.equals("eof")){
            ErrorHandler.addParserError(res.key, " : Syntax Error! Unexpected EndOfFile");
            throw new Exception("endOfFile");
        }
        if (!Character.isUpperCase(currentState.getNextStates().get(0).key.charAt(0))){
            ErrorHandler.addParserError(res.key, " : Syntax Error! Missing " + currentState.getNextStates().get(0).key);
            throw new Exception("error happened");
        } else {
            while (true){
                String token = res.value.key;
                Diagram expectedNonTerminal = getDiagram(currentState.getNextStates().get(0).key);
                assert expectedNonTerminal != null;
                if (expectedNonTerminal.getFirstSet().contains(token) || expectedNonTerminal.getFollowSet().contains(token)){
                    if (expectedNonTerminal.getFirstSet().contains(token) || expectedNonTerminal.isNullable){
                        continue;
                    }
                    ErrorHandler.addParserError(res.key, " : Syntax Error! Missing " + getNonTerminalDes(expectedNonTerminal));
                    currentState = currentState.getNextState(expectedNonTerminal.getName());
                    continue;
                }

                if (res.value.key.equals("EOF")) {
                    ErrorHandler.addParserError(res.key, " : Syntax Error! Malformed Input");
                    throw new Exception("endOfFile");
                } else {
                    ErrorHandler.addParserError(res.key, " : Syntax Error! Unexpected " + res.value);
                    res = lexer.get_next_token(codeAddress);
                }
            }
        }
    }

    private void printLayer(int layer, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < layer; i++) {
            sb.append("|");
        }
        sb.append(s);
        ParseHandler.add(sb.toString());
//        System.out.println(sb);
    }

    private String getNonTerminalDes(Diagram expectedNonTerminal) {
        switch (expectedNonTerminal.getName()){
            case "Program":
                return "Program";
            case "Declaration_list":
                return "a program";
            case "Declaration":
                return "a new part of program";
            case "AAA6":
                return "; or define function";
            case "AAA1":
                return "[]";
            case "Type_specifier":
                return "int or void";
            case "Params":
                return "parameters";
            case "AAA8":
                return "parameter details";
            case "AAA7":
                return "initial values";
            case "Param":
                return "a parameter";
            case "AAA2":
                return "[]";
            case "Compound_stmt":
                return "function body";
            case "Statement_list":
                return "some statements";
            case "Statement":
                return "a statement";
            case "Expression_stmt":
                return "an expression, break or continue";
            case "Selection_stmt":
                return "if statement";
            case "Iteration_stmt":
                return "while statement";
            case "Return_stmt":
                return "return";
            case "AAA3":
                return "an expression";
            case "Switch_stmt":
                return "switch";
            case "Case_stmts":
                return "some cases";
            case "Case_stmt":
                return " a case";
            case "Default_stmt":
                return"default statement";
            case "Expression":
                return "an expression";
            case "AAA13":
                return "[ or (";
            case "AAA14":
                return "= or *";
            case "AAA4":
                return "[";
            case "AAA5":
                return "< or ==";
            case "Relop":
                return "< or ==";
            case "AAA12":
                return "+ or -";
            case "Addop":
                return "+ or -";
            case "AAA11":
                return "*";
            case "Signed_factor":
                return "a number";
            case "Factor":
                return "a number";
            case "AAA10":
                return "[ or (";
            case "Args":
                return "some arguments";
            case "Arg_list":
                return "some arguments";
            case "AAA9":
                return ",";
        }

        return expectedNonTerminal.getName();
    }

    public Tuple<String, String> getNextTransaction(Diagram cDdiagram, State state, Tuple<String, String> nextToken) throws Exception {
        nextToken = toGrammarLang(nextToken);

        for (int i = 0; i < state.getNextStates().size() ; i++) {
            String tr = state.getNextStates().get(i).key;
            if (tr.equals(nextToken.key))
                return new Tuple<>(tr, nextToken.value);
        }

        for (int i = 0; i < state.getNextStates().size() ; i++) {
            if (state.getNextStates().get(i).key.equals("") || !Character.isUpperCase(state.getNextStates().get(i).key.charAt(0)))
                continue;
            Diagram diagram = getDiagram(state.getNextStates().get(i).key);

            assert diagram != null;
            if (diagram.getFirstSet().contains(nextToken.key))
                return new Tuple<>(state.getNextStates().get(i).key, nextToken.value);
        }

        for (int i = 0; i < state.getNextStates().size() ; i++) {
            if (state.getNextStates().get(i).key.equals("") || !Character.isUpperCase(state.getNextStates().get(i).key.charAt(0)))
                continue;
            Diagram diagram = getDiagram(state.getNextStates().get(i).key);

            assert diagram != null;
            if (diagram.isNullable)
                if (diagram.getFollowSet().contains(nextToken.key))
                    return new Tuple<>(state.getNextStates().get(i).key, nextToken.value);

        }

        if (cDdiagram.isNullable)
            if (cDdiagram.getFollowSet().contains(nextToken.key))
                return new Tuple<>("", nextToken.value);

        return null;
    }

    private Tuple<String, String> toGrammarLang(Tuple<String, String> nextToken) throws Exception {
        switch (nextToken.key) {
            case "KEYWORD":
                return new Tuple<>(nextToken.value, nextToken.value);
            case "ID":
                return new Tuple<>("id", nextToken.value);
            case "SYMBOL":
                switch (nextToken.value) {
                    case ",":
                        return new Tuple<>("camma", nextToken.value);
                    case "[":
                        return new Tuple<>("oc", nextToken.value);
                    case "]":
                        return new Tuple<>("cc", nextToken.value);
                    case "{":
                        return new Tuple<>("of", nextToken.value);
                    case "}":
                        return new Tuple<>("cf", nextToken.value);
                    case "-":
                        return new Tuple<>("minus", nextToken.value);
                    case "=":
                        return new Tuple<>("eq", nextToken.value);
                    case "==":
                        return new Tuple<>("~", nextToken.value);
                    case "<":
                        return new Tuple<>("lt", nextToken.value);
                    default:
                        return new Tuple<>(nextToken.value, nextToken.value);
                }
            case "NUM":
                return new Tuple<>("num", nextToken.value);
            case "EOF":
                return new Tuple<>("eof", "EOF");
        }
        throw new Exception("incorrect token");
    }

    public void createFF(String FFAddress){
        StringBuilder s = readFromFile(FFAddress);

        String[] lines = s.toString().split("\r\n");

        for (int i = 0; i < lines.length ; i++) {
            String[] words = lines[i].split("\t");
            Diagram diagram = getOrCreateDiagram(words[0]);
            String[] firsts = words[1].split(" ");
            String[] follows = words[2].split(" ");
            boolean isNullable = words[3].equals("yes");

            diagram.setFirstSet(firsts);
            if (!follows[0].equals(""))
                diagram.setFollowSet(follows);
            diagram.isNullable = isNullable;
        }

    }

    private StringBuilder readFromFile(String address){
        StringBuilder s = new StringBuilder();

        try{
            FileInputStream fin=new FileInputStream(address);
            int i;

            while((i=fin.read())!=-1){
                s.append((char)i);
            }

            fin.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return s;
    }

    public void createDiagrams(String grammarAddress){

        StringBuilder s = readFromFile(grammarAddress);

        String grammar = String.valueOf(s);

        s = new StringBuilder();

        for (int i = 0; i < grammar.length() ; i++) {
            if (grammar.charAt(i) == '\n' || grammar.charAt(i) == '\r')
                continue;
            if (grammar.charAt(i) == '.'){
                createOneLine(s.toString());
                s = new StringBuilder();
                continue;
            }
            s.append(grammar.charAt(i));
        }

    }

    private void createOneLine(String s){
        String[] strings = s.split(" ");
        if (strings.length == 2){
            Diagram diagram = getOrCreateDiagram(strings[0]);
            diagram.setToLastState("", diagram.getStartState());
            return;
        }

        Diagram diagram = getOrCreateDiagram(strings[0]);
        State state = diagram.getStartState();

        int i;
        for (i = 2; i < strings.length - 1 ; i++) {
            String cs = strings[i];
            state.setNextState(cs, new State(strings[i+1]));
            state = state.getNextState(cs);
        }
        String cs = strings[i];
        state.setNextState(cs, diagram.getEndState());

    }

    private Diagram getOrCreateDiagram(String diagramName){
        for (Diagram diagram1 : diagrams) {
            if (diagram1.getName().equals(diagramName))
                return diagram1;
        }
        Diagram diagram = new Diagram(diagramName);
        diagrams.add(diagram);

        return diagram;
    }

    private Diagram getDiagram(String diagramName){
        for (Diagram diagram : diagrams) {
            if (diagram.getName().equals(diagramName))
                return diagram;
        }
        return null;
    }

}
