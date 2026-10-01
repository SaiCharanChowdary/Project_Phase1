import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/** Loads the stop-word list (one word per line; surrounding spaces and CRLF are ignored). */
public class StopWordList {

    private final Set<String> stopWords = new HashSet<String>();

    public StopWordList(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String word = line.trim().toLowerCase();
                if (!word.isEmpty()) {
                    stopWords.add(word);
                }
            }
        } finally {
            reader.close();
        }
    }

    public boolean isStopWord(String token) {
        return stopWords.contains(token);
    }

    public int size() {
        return stopWords.size();
    }
}
