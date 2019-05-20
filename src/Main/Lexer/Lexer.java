package Main.Lexer;

import Main.PriorityRegexDictionary.PriorityRegexDictionary;
import Main.PriorityRegexDictionary.Tuple;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lexer {
    private Reader r;

    private static Map<State, PriorityRegexDictionary<State>> table = new HashMap<>();

    static {
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
        table.get(State.I).put("[A-Za-z0-9]", State.Id);


        table.put(State.Digit, new PriorityRegexDictionary<>());
        table.get(State.Digit).put("[0-9]", State.Digit);


        table.put(State.E, new PriorityRegexDictionary<>());
        table.get(State.E).put("l", State.El);
        table.get(State.E).put("[A-Za-z0-9]", State.Id);


        table.put(State.V, new PriorityRegexDictionary<>());
        table.get(State.V).put("o", State.Vo);
        table.get(State.V).put("[A-Za-z0-9]", State.Id);


        table.put(State.W, new PriorityRegexDictionary<>());
        table.get(State.W).put("h", State.Wh);
        table.get(State.W).put("[A-Za-z0-9]", State.Id);


        table.put(State.B, new PriorityRegexDictionary<>());
        table.get(State.B).put("r", State.Br);
        table.get(State.B).put("[A-Za-z0-9]", State.Id);


        table.put(State.C, new PriorityRegexDictionary<>());
        table.get(State.C).put("o", State.Co);
        table.get(State.C).put("a", State.Ca);
        table.get(State.C).put("[A-Za-z0-9]", State.Id);


        table.put(State.S, new PriorityRegexDictionary<>());
        table.get(State.S).put("w", State.Sw);
        table.get(State.S).put("[A-Za-z0-9]", State.Id);


        table.put(State.D, new PriorityRegexDictionary<>());
        table.get(State.D).put("i", State.De);
        table.get(State.D).put("[A-Za-z0-9]", State.Id);


        table.put(State.R, new PriorityRegexDictionary<>());
        table.get(State.R).put("e", State.Re);
        table.get(State.R).put("[A-Za-z0-9]", State.Id);


        table.put(State.If, new PriorityRegexDictionary<>());
        table.get(State.If).put("[A-Za-z0-9]", State.Id);


        table.put(State.In, new PriorityRegexDictionary<>());
        table.get(State.In).put("t", State.Int);
        table.get(State.In).put("[A-Za-z0-9]", State.Id);


        table.put(State.Int, new PriorityRegexDictionary<>());
        table.get(State.Int).put("[A-Za-z0-9]", State.Id);


        table.put(State.El, new PriorityRegexDictionary<>());
        table.get(State.El).put("s", State.Els);
        table.get(State.El).put("[A-Za-z0-9]", State.Id);


        table.put(State.Els, new PriorityRegexDictionary<>());
        table.get(State.Els).put("e", State.Else);
        table.get(State.Els).put("[A-Za-z0-9]", State.Id);


        table.put(State.Else, new PriorityRegexDictionary<>());
        table.get(State.Else).put("[A-Za-z0-9]", State.Id);


        table.put(State.Vo, new PriorityRegexDictionary<>());
        table.get(State.Vo).put("i", State.Voi);
        table.get(State.Vo).put("[A-Za-z0-9]", State.Id);


        table.put(State.Voi, new PriorityRegexDictionary<>());
        table.get(State.Voi).put("d", State.Void);
        table.get(State.Voi).put("[A-Za-z0-9]", State.Id);


        table.put(State.Void, new PriorityRegexDictionary<>());
        table.get(State.Void).put("[A-Za-z0-9]", State.Id);


        table.put(State.Wh, new PriorityRegexDictionary<>());
        table.get(State.Wh).put("i", State.Whi);
        table.get(State.Wh).put("[A-Za-z0-9]", State.Id);


        table.put(State.Whi, new PriorityRegexDictionary<>());
        table.get(State.Whi).put("l", State.Whil);
        table.get(State.Whi).put("[A-Za-z0-9]", State.Id);


        table.put(State.Whil, new PriorityRegexDictionary<>());
        table.get(State.Whil).put("e", State.While);
        table.get(State.Whil).put("[A-Za-z0-9]", State.Id);


        table.put(State.While, new PriorityRegexDictionary<>());
        table.get(State.While).put("[A-Za-z0-9]", State.Id);


        table.put(State.Br, new PriorityRegexDictionary<>());
        table.get(State.Br).put("e", State.Bre);
        table.get(State.Br).put("[A-Za-z0-9]", State.Id);


        table.put(State.Bre, new PriorityRegexDictionary<>());
        table.get(State.Bre).put("a", State.Brea);
        table.get(State.Bre).put("[A-Za-z0-9]", State.Id);


        table.put(State.Brea, new PriorityRegexDictionary<>());
        table.get(State.Brea).put("k", State.Break);
        table.get(State.Brea).put("[A-Za-z0-9]", State.Id);


        table.put(State.Break, new PriorityRegexDictionary<>());
        table.get(State.Break).put("[A-Za-z0-9]", State.Id);


        table.put(State.Co, new PriorityRegexDictionary<>());
        table.get(State.Co).put("n", State.Con);
        table.get(State.Co).put("[A-Za-z0-9]", State.Id);


        table.put(State.Con, new PriorityRegexDictionary<>());
        table.get(State.Con).put("t", State.Cont);
        table.get(State.Con).put("[A-Za-z0-9]", State.Id);


        table.put(State.Cont, new PriorityRegexDictionary<>());
        table.get(State.Cont).put("i", State.Conti);
        table.get(State.Cont).put("[A-Za-z0-9]", State.Id);


        table.put(State.Conti, new PriorityRegexDictionary<>());
        table.get(State.Conti).put("n", State.Contin);
        table.get(State.Conti).put("[A-Za-z0-9]", State.Id);


        table.put(State.Contin, new PriorityRegexDictionary<>());
        table.get(State.Contin).put("u", State.Continu);
        table.get(State.Contin).put("[A-Za-z0-9]", State.Id);


        table.put(State.Continu, new PriorityRegexDictionary<>());
        table.get(State.Continu).put("e", State.Continue);
        table.get(State.Continu).put("[A-Za-z0-9]", State.Id);


        table.put(State.Continue, new PriorityRegexDictionary<>());
        table.get(State.Continue).put("[A-Za-z0-9]", State.Id);


        table.put(State.Ca, new PriorityRegexDictionary<>());
        table.get(State.Ca).put("s", State.Cas);
        table.get(State.Ca).put("[A-Za-z0-9]", State.Id);


        table.put(State.Cas, new PriorityRegexDictionary<>());
        table.get(State.Cas).put("e", State.Case);
        table.get(State.Cas).put("[A-Za-z0-9]", State.Id);


        table.put(State.Case, new PriorityRegexDictionary<>());
        table.get(State.Case).put("[A-Za-z0-9]", State.Id);


        table.put(State.Sw, new PriorityRegexDictionary<>());
        table.get(State.Sw).put("i", State.Swi);
        table.get(State.Sw).put("[A-Za-z0-9]", State.Id);


        table.put(State.Swi, new PriorityRegexDictionary<>());
        table.get(State.Swi).put("t", State.Swit);
        table.get(State.Swi).put("[A-Za-z0-9]", State.Id);


        table.put(State.Swit, new PriorityRegexDictionary<>());
        table.get(State.Swit).put("c", State.Switc);
        table.get(State.Swit).put("[A-Za-z0-9]", State.Id);


        table.put(State.Switc, new PriorityRegexDictionary<>());
        table.get(State.Switc).put("h", State.Switch);
        table.get(State.Switc).put("[A-Za-z0-9]", State.Id);


        table.put(State.Switch, new PriorityRegexDictionary<>());
        table.get(State.Switch).put("[A-Za-z0-9]", State.Id);


        table.put(State.De, new PriorityRegexDictionary<>());
        table.get(State.De).put("f", State.Def);
        table.get(State.De).put("[A-Za-z0-9]", State.Id);


        table.put(State.Def, new PriorityRegexDictionary<>());
        table.get(State.Def).put("a", State.Defa);
        table.get(State.Def).put("[A-Za-z0-9]", State.Id);


        table.put(State.Defa, new PriorityRegexDictionary<>());
        table.get(State.Defa).put("u", State.Defau);
        table.get(State.Defa).put("[A-Za-z0-9]", State.Id);


        table.put(State.Defau, new PriorityRegexDictionary<>());
        table.get(State.Defau).put("l", State.Defaul);
        table.get(State.Defau).put("[A-Za-z0-9]", State.Id);


        table.put(State.Defaul, new PriorityRegexDictionary<>());
        table.get(State.Defaul).put("t", State.Default);
        table.get(State.Defaul).put("[A-Za-z0-9]", State.Id);


        table.put(State.Default, new PriorityRegexDictionary<>());
        table.get(State.Default).put("[A-Za-z0-9]", State.Id);


        table.put(State.Re, new PriorityRegexDictionary<>());
        table.get(State.Re).put("t", State.Ret);
        table.get(State.Re).put("[A-Za-z0-9]", State.Id);


        table.put(State.Ret, new PriorityRegexDictionary<>());
        table.get(State.Ret).put("u", State.Retu);
        table.get(State.Ret).put("[A-Za-z0-9]", State.Id);


        table.put(State.Retu, new PriorityRegexDictionary<>());
        table.get(State.Retu).put("r", State.Retur);
        table.get(State.Retu).put("[A-Za-z0-9]", State.Id);


        table.put(State.Retur, new PriorityRegexDictionary<>());
        table.get(State.Retur).put("n", State.Return);
        table.get(State.Retur).put("[A-Za-z0-9]", State.Id);


        table.put(State.Return, new PriorityRegexDictionary<>());
        table.get(State.Return).put("[A-Za-z0-9]", State.Id);


        table.put(State.Id, new PriorityRegexDictionary<>());
        table.get(State.Id).put("[A-Za-z0-9]", State.Id);


        table.put(State.Eof, new PriorityRegexDictionary<>());


        table.put(State.Symbol, new PriorityRegexDictionary<>());
        table.get(State.Symbol).put("=", State.Symbol_eq);


        table.put(State.Symbol_eq, new PriorityRegexDictionary<>());


        table.put(State.Slash, new PriorityRegexDictionary<>());
        table.get(State.Slash).put("\\*", State.Slash_Star);
        table.get(State.Slash).put("/", State.Slash_Slash);


//        table.put(State.Slash_Star, new PriorityRegexDictionary<>());
//        table.get(State.Slash_Star).put(".|\\n|\\t|\\r|\\f|\\v", State.Slash_Star);


        table.put(State.Slash_Star, new PriorityRegexDictionary<>());
        table.get(State.Slash_Star).put("\\*", State.Slash_Star_Star);
        table.get(State.Slash_Star).put(".|\\n|\\t|\\r|\\f|\\v", State.Slash_Star);


        table.put(State.Slash_Star_Star, new PriorityRegexDictionary<>());
        table.get(State.Slash_Star_Star).put("/", State.Slash_Star_Star_Slash);
        table.get(State.Slash_Star_Star).put(".|\\n|\\t|\\r|\\f|\\v", State.Slash_Star);


        table.put(State.Slash_Star_Star_Slash, new PriorityRegexDictionary<>());


        table.put(State.Slash_Slash, new PriorityRegexDictionary<>());
        table.get(State.Slash_Slash).put("\\n", State.Slash_Slash_Enter);
        table.get(State.Slash_Slash).put(".|\\n|\\t|\\r|\\f|\\v", State.Slash_Slash);


        table.put(State.Slash_Slash_Enter, new PriorityRegexDictionary<>());


        table.put(State.White, new PriorityRegexDictionary<>());
        table.get(State.White).put("\\n|\\t|\\r|\\f|\\v| ", State.White);

    }

    private State findNextState(State state, String input) {
        return table.get(state).get(input);
    }

    private boolean isValidInput(char c) {
        State s = State.Start;
        return table.get(s).get(Character.toString(c)) != null;
    }

    private int nextChar;
    private int line = 1;

    private Tuple<Integer, Tuple<Tuple<String, String>, String>> getNextToken() throws IOException { //line, lexer gp, word, error
        State state = State.Start;
        Tuple<String, String> res = new Tuple<>();
        StringBuilder sb = new StringBuilder();
        State preState;
//        int startLine = line;
        int currentChar = nextChar;
        while (currentChar != -1) {
            char ch = (char) currentChar;
            preState = state;
            state = findNextState(state, Character.toString(ch));
            if (state != null) {
                sb.append(ch);
            } else if (isValidInput(ch)) {
                res.key = State.getTokenName(preState);
                res.value = sb.toString();
                return new Tuple<>(line, new Tuple<>(res, null));
            } else {
                sb.append(ch);
                nextChar = r.read();
                return new Tuple<>(line, new Tuple<>(null, sb.toString()));
            }

            if (ch == '\n')
                line++;

            nextChar = r.read();
            currentChar = nextChar;
        }

        if (state == State.Start)
            return null;

        if (state == State.Slash_Slash)
            state = State.Slash_Slash_Enter;
        if (State.getTokenName(state) != null) {
            res.key = State.getTokenName(state);
            res.value = sb.toString();
            return new Tuple<>(line, new Tuple<>(res, null));
        } else {
            return new Tuple<>(line, new Tuple<>(null, sb.toString()));
        }

    }

    public Tuple<ArrayList<Tuple<Integer, Tuple<String, String>>>, ArrayList<Tuple<Integer, String>>> analyse(String fileName) throws IOException {
        ArrayList<Tuple<Integer, Tuple<String, String>>> res = new ArrayList<>();
        ArrayList<Tuple<Integer, String>> errors = new ArrayList<>();

        InputStream in = new FileInputStream(fileName);
        r = new InputStreamReader(in, "UTF-8");

        Tuple<Integer, Tuple<Tuple<String, String>, String>> t;
        nextChar = r.read();
        do {
            t = getNextToken();
            if (t != null) {
                if (t.value.key == null)
                    errors.add(new Tuple<>(t.key, t.value.value));
                else
                    res.add(new Tuple<>(t.key, t.value.key));
            } else
                break;
        } while (true);

        return new Tuple<>(res, errors);
    }

}
