package Main.PriorityRegexDictionary;


public class Tuple<K, V>{
    public K key;
    public V value;

    public Tuple(K key, V value){
        this.key = key;
        this.value = value;
    }

    public Tuple() {

    }

    @Override
    public String toString() {
        return "[ " + key.toString() + ", " + value.toString() + " ]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass())
            return false;
        Tuple t = (Tuple) obj;
        return t.key.equals(this.key) && t.value.equals(this.value);
    }
}
