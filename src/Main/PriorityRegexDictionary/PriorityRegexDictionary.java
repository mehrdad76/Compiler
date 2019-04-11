package Main.PriorityRegexDictionary;

import java.util.*;
import java.util.regex.*;

public class PriorityRegexDictionary<V> {
    private ArrayList<Tuple<String, V>> list = new ArrayList<>();
    private HashSet<String> keys = new HashSet<>();
    private HashSet<V> values = new HashSet<>();

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.size() == 0;
    }

    public boolean containsKey(String key) {
        return keys.contains(key);
    }

    public boolean containsValue(V value) {
        return values.contains(value);
    }

    public V get(String key) {
        Pattern pat;
        Matcher m;
        for (int i = 0; i < list.size(); i++) {
            pat = Pattern.compile(list.get(i).key);
            m = pat.matcher(key);

            if (m.matches())
                return list.get(i).value;
        }
        return null;
    }

    public void put(String key, V value) {
        list.add(new Tuple<>(key, value));
        keys.add(key);
        values.add(value);
    }

    public Object remove(String key) {
        if (!containsKey(key)){
            return null;
        }

        int i;
        for (i = 0; i < list.size(); i++) {
            if (list.get(i).key.equals(key))
                break;
        }

        V v = list.get(i).value;
        keys.remove(key);
        values.remove(v);
        list.remove(i);

        return v;
    }

    public void clear() {
        list.clear();
        keys.clear();
        values.clear();
    }

    public Set keySet() {
        return keys;
    }

    public Collection values() {
        return values;
    }

}
