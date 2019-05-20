package Main.Lexer;

import java.util.HashSet;

public enum State {
    Start,
    I,
    E,
    V,
    W,
    B,
    C,
    S,
    D,
    R,
    If,
    In,
    Int,
    El,
    Els,
    Else,
    Vo,
    Voi,
    Void,
    Wh,
    Whi,
    Whil,
    While,
    Br,
    Bre,
    Brea,
    Break,
    Co,
    Con,
    Cont,
    Conti,
    Contin,
    Continu,
    Continue,
    Ca,
    Cas,
    Case,
    Sw,
    Swi,
    Swit,
    Switc,
    Switch,
    De,
    Def,
    Defa,
    Defau,
    Defaul,
    Default,
    Re,
    Ret,
    Retu,
    Retur,
    Return,
    Id,
    Digit,
    Eof,
    Symbol,
    Symbol_eq,
    Slash,
    Slash_Star,
    Slash_Star_Star,
    Slash_Star_Star_Slash,
    Slash_Slash,
    Slash_Slash_Enter,
    White;

    public static String getTokenName(State s){
        HashSet<State> num = new HashSet<>();
        num.add(Digit);

        HashSet<State> id = new HashSet<>();
        id.add(I);
        id.add(E);
        id.add(V);
        id.add(W);
        id.add(B);
        id.add(C);
        id.add(S);
        id.add(D);
        id.add(R);
        id.add(In);
        id.add(El);
        id.add(Els);
        id.add(Vo);
        id.add(Voi);
        id.add(Wh);
        id.add(Whi);
        id.add(Whil);
        id.add(Br);
        id.add(Bre);
        id.add(Brea);
        id.add(Co);
        id.add(Con);
        id.add(Cont);
        id.add(Conti);
        id.add(Contin);
        id.add(Continu);
        id.add(Ca);
        id.add(Cas);
        id.add(Sw);
        id.add(Swi);
        id.add(Swit);
        id.add(Switc);
        id.add(De);
        id.add(Def);
        id.add(Defa);
        id.add(Defau);
        id.add(Defaul);
        id.add(Re);
        id.add(Ret);
        id.add(Retu);
        id.add(Retur);
        id.add(Id);

        HashSet<State> keyword = new HashSet<>();
        keyword.add(If);
        keyword.add(Int);
        keyword.add(Else);
        keyword.add(Void);
        keyword.add(While);
        keyword.add(Break);
        keyword.add(Continue);
        keyword.add(Case);
        keyword.add(Switch);
        keyword.add(Default);
        keyword.add(Return);

        HashSet<State> symbol = new HashSet<>();
        symbol.add(Symbol);
        symbol.add(Symbol_eq);

        HashSet<State> comment = new HashSet<>();
        comment.add(Slash_Star_Star_Slash);
        comment.add(Slash_Slash_Enter);

        HashSet<State> whiteSpace = new HashSet<>();
        whiteSpace.add(White);

        HashSet<State> eof = new HashSet<>();
        eof.add(Eof);

//        HashSet<State> bug = new HashSet<>();
//        bug.add(Start);
//        bug.add(Slash);
//        bug.add(Slash_Star);
//        bug.add(Slash_Star_Star);
//        bug.add(Slash_Slash);

        if (num.contains(s))
            return "NUM";

        if (id.contains(s))
            return "ID";

        if (keyword.contains(s))
            return "KEYWORD";

        if (symbol.contains(s))
            return "SYMBOL";

        if (comment.contains(s))
            return "COMMENT";

        if (whiteSpace.contains(s))
            return "WHITESPACE";

        if (eof.contains(s))
            return "EOF";

        return null;
    }
}
