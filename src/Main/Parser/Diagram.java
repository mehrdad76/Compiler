package Main.Parser;

import Main.PriorityRegexDictionary.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Diagram {
    private State startState;
    private State endState;
    private String DName;
    boolean isNullable;

    private HashSet<String> followSet = new HashSet<>();
    private HashSet<String> firstSet = new HashSet<>();

    public HashSet<String> getFirstSet() {
        return firstSet;
    }

    public HashSet<String> getFollowSet() {
        return followSet;
    }

    public void setFirstSet(String[] set) {
        firstSet.addAll(Arrays.asList(set));
    }

    public void setFollowSet(String[] set) {
        followSet.addAll(Arrays.asList(set));
    }

    public String getName() {
        return DName;
    }

    public State getStartState() {
        return startState;
    }

    public State getEndState() {
        return endState;
    }

    public Diagram(String name){
        this.DName = name;
        startState = new State(name);
        endState = new State("end");
    }

    public void setToLastState(String t, State state) {
        state.setNextState(t, endState);
    }

    @Override
    public String toString() {
        return DName;
    }
}
