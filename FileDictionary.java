import java.util.LinkedHashMap;
import java.util.Map;

/** FileDictionary: maps each document name (DOCNO) to a unique numerical ID, in the order documents are read. */
public class FileDictionary {

    private final LinkedHashMap<String, Integer> docToId = new LinkedHashMap<String, Integer>();
    private int nextId = 1;

    /** Adds the document if new and returns its ID. */
    public int add(String docName) {
        Integer id = docToId.get(docName);
        if (id == null) {
            id = nextId++;
            docToId.put(docName, id);
        }
        return id;
    }

    public int getId(String docName) {
        Integer id = docToId.get(docName);
        return id == null ? -1 : id;
    }

    public Map<String, Integer> entries() {
        return docToId;
    }

    public int size() {
        return docToId.size();
    }
}
