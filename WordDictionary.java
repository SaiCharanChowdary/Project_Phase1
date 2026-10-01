import java.util.Map;
import java.util.TreeMap;

/**
 * WordDictionary: maps every unique (stemmed) term to a unique numerical ID.
 * Terms are kept in alphabetical order, and IDs 1..N are assigned in that order
 * once all documents have been parsed (see assignIds()).
 */
public class WordDictionary {

    private final TreeMap<String, Integer> termToId = new TreeMap<String, Integer>();

    /** Record a term (its ID is assigned later by assignIds()). */
    public void add(String term) {
        if (!termToId.containsKey(term)) {
            termToId.put(term, 0);
        }
    }

    /** Assign IDs 1..N in alphabetical order of the terms. */
    public void assignIds() {
        int id = 1;
        for (Map.Entry<String, Integer> entry : termToId.entrySet()) {
            entry.setValue(id++);
        }
    }

    /** Returns the term's ID, or -1 if the term is not in the dictionary. */
    public int getId(String term) {
        Integer id = termToId.get(term);
        return id == null ? -1 : id;
    }

    public Map<String, Integer> entries() {
        return termToId;
    }

    public int size() {
        return termToId.size();
    }
}
