package Main.Parser;

import Main.PriorityRegexDictionary.Tuple;

import java.util.ArrayList;

public class State {
    private String SName;
    private ArrayList<Tuple<String, State>> nextStates = new ArrayList<>();

    public State getNextState(String t) {
        for (int i = 0; i < nextStates.size() ; i++) {
            if (nextStates.get(i).key.equals(t))
                return nextStates.get(i).value;
        }
        return null;
    }

    public ArrayList<Tuple<String, State>> getNextStates() {
        return nextStates;
    }

    public void setNextState(String t, State nextState) {
        this.nextStates.add(new Tuple<>(t, nextState));
    }

    @Override
    public boolean equals(Object obj) {
        State s = (State) obj;

        return s.SName.equals(SName);
    }

    @Override
    public String toString() {
        return SName;
    }

    public String getSName() {
        return SName;
    }

    public State(String name){
        this.SName = name;
    }

}


