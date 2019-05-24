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

                        ErrorHandler.addParserError(res.key, " : Syntax Error! Unexpected " + res.value);
                        res = lexer.get_next_token(codeAddress);
                        if (res.value.key.equals("EOF")) {
                            ErrorHandler.addParserError(res.key, " : Syntax Error! Malformed Input");
                            throw new Exception("endOfFile");
                        }
                    }
                }
            }


            String tr = aux.key;
            String seen = aux.value;

            if (tr.equals("")) {
                currentState = currentState.getNextState(tr);
                printLayer(layer+1, "epsilon");
            } else if(!Character.isUpperCase(tr.charAt(0))){
                currentState = currentState.getNextState(tr);
                printLayer(layer+1, seen);
                res = lexer.get_next_token(codeAddress);
                isNext = false;
            } else {
                Diagram diagram = getDiagram(tr);
                assert diagram != null;
                parse(lexer, codeAddress, diagram, diagram.getStartState(), false, layer + 1);
                currentState = currentState.getNextState(tr);
                isNext = false;
            }


        } while (!res.value.key.equals("EOF"));

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
        return "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO";
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
