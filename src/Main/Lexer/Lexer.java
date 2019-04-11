package Main.Lexer;

import Main.PriorityRegexDictionary.PriorityRegexDictionary;
import Main.PriorityRegexDictionary.Tuple;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    InputStream in;
    Reader r;

    private HashMap<State, PriorityRegexDictionary<State>> table = new HashMap<>();

    {
        table.put(State.Start, new PriorityRegexDictionary<>());
        table.get(State.Start).put("[0-9]", State.Digit);
        table.get(State.Start).put("\\n|\\t|\\r|\\f|\\v| ", State.White);
        table.get(State.Start).put("/", State.Slash);
        table.get(State.Start).put(";|:|,|\\[|\\]|\\(|\\)|\\{|\\}|\\+|-|\\*|=|<", State.Symbol);
        table.get(State.Start).put("eof", State.Eof);
        table.get(State.Start).put("i", State.I);
        table.get(State.Start).put("e", State.E);
        table.get(State.Start).put("v", State.V);
        table.get(State.Start).put("w", State.W);
        table.get(State.Start).put("b", State.B);
        table.get(State.Start).put("c", State.C);
        table.get(State.Start).put("s", State.S);
        table.get(State.Start).put("d", State.D);
        table.get(State.Start).put("r", State.R);
        table.get(State.Start).put("[A-Za-z]", State.Id);


        table.put(State.I, new PriorityRegexDictionary<>());
        table.get(State.I).put("f", State.If);
        table.get(State.I).put("n", State.In);


        table.put(State.Digit, new PriorityRegexDictionary<>());
        table.get(State.Digit).put("[0-9]", State.Digit);


        table.put(State.E, new PriorityRegexDictionary<>());
        table.get(State.E).put("l", State.El);


        table.put(State.V, new PriorityRegexDictionary<>());
        table.get(State.V).put("o", State.Vo);


        table.put(State.W, new PriorityRegexDictionary<>());
        table.get(State.W).put("h", State.Wh);


        table.put(State.B, new PriorityRegexDictionary<>());
        table.get(State.B).put("r", State.Br);


        table.put(State.C, new PriorityRegexDictionary<>());
        table.get(State.C).put("o", State.Co);
        table.get(State.C).put("a", State.Ca);


        table.put(State.S, new PriorityRegexDictionary<>());
        table.get(State.S).put("w", State.Sw);


        table.put(State.D, new PriorityRegexDictionary<>());
        table.get(State.D).put("i", State.De);


        table.put(State.R, new PriorityRegexDictionary<>());
        table.get(State.R).put("e", State.Re);


        table.put(State.If, new PriorityRegexDictionary<>());
        table.get(State.If).put("[A-Za-z0-9]", State.Id);


        table.put(State.In, new PriorityRegexDictionary<>());
        table.get(State.In).put("t", State.Int);


        table.put(State.Int, new PriorityRegexDictionary<>());
        table.get(State.Int).put("[A-Za-z0-9]", State.Id);


        table.put(State.El, new PriorityRegexDictionary<>());
        table.get(State.El).put("s", State.Els);


        table.put(State.Els, new PriorityRegexDictionary<>());
        table.get(State.Els).put("e", State.Else);


        table.put(State.Else, new PriorityRegexDictionary<>());
        table.get(State.Else).put("[A-Za-z0-9]", State.Id);


        table.put(State.Vo, new PriorityRegexDictionary<>());
        table.get(State.Vo).put("i", State.Voi);


        table.put(State.Voi, new PriorityRegexDictionary<>());
        table.get(State.Voi).put("d", State.Void);


        table.put(State.Void, new PriorityRegexDictionary<>());
        table.get(State.Void).put("[A-Za-z0-9]", State.Id);


        table.put(State.Wh, new PriorityRegexDictionary<>());
        table.get(State.Wh).put("i", State.Whi);


        table.put(State.Whi, new PriorityRegexDictionary<>());
        table.get(State.Whi).put("l", State.Whil);


        table.put(State.Whil, new PriorityRegexDictionary<>());
        table.get(State.Whil).put("e", State.While);


        table.put(State.While, new PriorityRegexDictionary<>());
        table.get(State.While).put("[A-Za-z0-9]", State.Id);


        table.put(State.Br, new PriorityRegexDictionary<>());
        table.get(State.Br).put("e", State.Bre);


        table.put(State.Bre, new PriorityRegexDictionary<>());
        table.get(State.Bre).put("a", State.Brea);


        table.put(State.Brea, new PriorityRegexDictionary<>());
        table.get(State.Brea).put("k", State.Break);


        table.put(State.Break, new PriorityRegexDictionary<>());
        table.get(State.Break).put("[A-Za-z0-9]", State.Id);


        table.put(State.Co, new PriorityRegexDictionary<>());
        table.get(State.Co).put("n", State.Con);


        table.put(State.Con, new PriorityRegexDictionary<>());
        table.get(State.Con).put("t", State.Cont);


        table.put(State.Cont, new PriorityRegexDictionary<>());
        table.get(State.Cont).put("i", State.Conti);


        table.put(State.Conti, new PriorityRegexDictionary<>());
        table.get(State.Conti).put("n", State.Contin);


        table.put(State.Contin, new PriorityRegexDictionary<>());
        table.get(State.Contin).put("u", State.Continu);


        table.put(State.Continu, new PriorityRegexDictionary<>());
        table.get(State.Continu).put("e", State.Continue);


        table.put(State.Continue, new PriorityRegexDictionary<>());
        table.get(State.Continue).put("[A-Za-z0-9]", State.Id);


        table.put(State.Ca, new PriorityRegexDictionary<>());
        table.get(State.Ca).put("s", State.Cas);


        table.put(State.Cas, new PriorityRegexDictionary<>());
        table.get(State.Cas).put("e", State.Case);


        table.put(State.Case, new PriorityRegexDictionary<>());
        table.get(State.Case).put("[A-Za-z0-9]", State.Id);


        table.put(State.Sw, new PriorityRegexDictionary<>());
        table.get(State.Sw).put("i", State.Swi);


        table.put(State.Swi, new PriorityRegexDictionary<>());
        table.get(State.Swi).put("t", State.Swit);


        table.put(State.Swit, new PriorityRegexDictionary<>());
        table.get(State.Swit).put("c", State.Switc);


        table.put(State.Switc, new PriorityRegexDictionary<>());
        table.get(State.Switc).put("h", State.Switch);


        table.put(State.Switch, new PriorityRegexDictionary<>());
        table.get(State.Switch).put("[A-Za-z0-9]", State.Id);


        table.put(State.De, new PriorityRegexDictionary<>());
        table.get(State.De).put("f", State.Def);


        table.put(State.Def, new PriorityRegexDictionary<>());
        table.get(State.Def).put("a", State.Defa);


        table.put(State.Defa, new PriorityRegexDictionary<>());
        table.get(State.Defa).put("u", State.Defau);


        table.put(State.Defau, new PriorityRegexDictionary<>());
        table.get(State.Defau).put("l", State.Defaul);


        table.put(State.Defaul, new PriorityRegexDictionary<>());
        table.get(State.Defaul).put("t", State.Default);


        table.put(State.Default, new PriorityRegexDictionary<>());
        table.get(State.Default).put("[A-Za-z0-9]", State.Id);


        table.put(State.Re, new PriorityRegexDictionary<>());
        table.get(State.Re).put("t", State.Ret);


        table.put(State.Ret, new PriorityRegexDictionary<>());
        table.get(State.Ret).put("u", State.Retu);


        table.put(State.Retu, new PriorityRegexDictionary<>());
        table.get(State.Retu).put("r", State.Retur);


        table.put(State.Retur, new PriorityRegexDictionary<>());
        table.get(State.Retur).put("n", State.Return);


        table.put(State.Return, new PriorityRegexDictionary<>());
        table.get(State.Return).put("[A-Za-z0-9]", State.Id);


        table.put(State.Id, new PriorityRegexDictionary<>());
        table.get(State.Id).put("[A-Za-z0-9]", State.Id);


        table.put(State.Digit, new PriorityRegexDictionary<>());
        table.get(State.Digit).put("[0-9]", State.Digit);


        table.put(State.Eof, new PriorityRegexDictionary<>());


        table.put(State.Symbol, new PriorityRegexDictionary<>());
        table.get(State.Symbol).put("=", State.Symbol_eq);


        table.put(State.Symbol_eq, new PriorityRegexDictionary<>());


        table.put(State.Slash, new PriorityRegexDictionary<>());
        table.get(State.Slash).put("\\*", State.Slash_Star);
        table.get(State.Slash).put("/", State.Slash_Slash);


        table.put(State.Slash_Star, new PriorityRegexDictionary<>());
        table.get(State.Slash_Star).put(".|\\n|\\t|\\r|\\f|\\v", State.Slash_Star);


        table.put(State.Slash_Star, new PriorityRegexDictionary<>());
        table.get(State.Slash_Star).put("\\*", State.Slash_Star_Star);
        table.get(State.Slash_Star).put(".|\\n|\\t|\\r|\\f|\\v", State.Slash_Star);


        table.put(State.Slash_Star, new PriorityRegexDictionary<>());
        table.get(State.Slash_Star).put("/", State.Slash_Star_Star_Slash);
        table.get(State.Slash_Star).put(".|\\n|\\t|\\r|\\f|\\v", State.Slash_Star);


        table.put(State.Slash_Star_Star_Slash, new PriorityRegexDictionary<>());


        table.put(State.Slash_Slash, new PriorityRegexDictionary<>());
        table.get(State.Slash_Slash).put("\\n", State.Slash_Slash_Enter);
        table.get(State.Slash_Slash).put(".|\\n|\\t|\\r|\\f|\\v", State.Slash_Slash);


        table.put(State.Slash_Slash_Enter, new PriorityRegexDictionary<>());


        table.put(State.White, new PriorityRegexDictionary<>());
        table.get(State.White).put("\\n|\\t|\\r|\\f|\\v| ", State.White);

    }

    public State findNextState(State state, String input){
        return table.get(state).get(input);
    }

    public boolean isValidInput(char c){
        State s = State.Start;
        return table.get(s).get(Character.toString(c)) != null;
    }

    private int currentChar;
    private int nextChar;
    private int line = 1;

    public Tuple<Integer, Tuple<Tuple<String, String>, String>> getNextToken() throws IOException { //line, lexer gp, word, error
        State state = State.Start;
        Tuple<String, String> res = new Tuple<>();
        StringBuilder sb = new StringBuilder();
        State preState;
        int startLine = line;
        currentChar = nextChar;
        while (currentChar != -1) {
            char ch = (char) currentChar;

            if (ch == '\n')
                line ++;

            preState = state;
            state = findNextState(state, Character.toString(ch));
            if (state != null){
                sb.append(ch);
            } else if(isValidInput(ch)){
                res.key = State.getTokenName(preState);
                res.value = sb.toString();
                return new Tuple<>(startLine, new Tuple<>(res, null));
            } else {
                sb.append(ch);
                nextChar = r.read();
                return new Tuple<>(startLine, new Tuple<>(null, sb.toString()));
            }
            nextChar = r.read();
            currentChar = nextChar;
        }

        if (state == State.Start)
            return null;

        if (state == State.Slash_Slash)
            state = State.Slash_Slash_Enter;
        if(State.getTokenName(state) != null){
            res.key = State.getTokenName(state);
            res.value = sb.toString();
            return new Tuple<>(startLine, new Tuple<>(res, null));
        } else {
            return new Tuple<>(startLine, new Tuple<>(null, sb.toString()));
        }

    }

    public Tuple<ArrayList<Tuple<Integer, Tuple<String, String>>>, ArrayList<Tuple<Integer, String>>> analyse(String fileName) throws IOException {
        ArrayList<Tuple<Integer, Tuple<String, String>>> res = new ArrayList<>();
        ArrayList<Tuple<Integer, String>> errors = new ArrayList<>();

        in = new FileInputStream(fileName);
        r = new InputStreamReader(in, "UTF-8");

        Tuple<Integer, Tuple<Tuple<String, String>, String>> t;
        nextChar = r.read();
        do {
            t = getNextToken();
            if (t != null){
                if (t.value.key == null)
                    errors.add(new Tuple<>(t.key, t.value.value));
                else
                    res.add(new Tuple<>(t.key, t.value.key));
            }
            else
                break;
        } while (true);

        return new Tuple<>(res, errors);
    }

}
