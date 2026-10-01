import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizer: turns raw document text into a stream of lower-case word tokens.
 *
 * Rules (from the assignment):
 *   - split on every non-alphanumeric character (punctuation, spaces, hyphens, apostrophes, ...)
 *   - remove numbers, and ignore any token that contains a digit
 *   - convert to lower case
 */
public class Tokenizer {

    /** Decode the few SGML entities that appear in the TREC FT data. */
    private static String decodeEntities(String text) {
        return text.replace("&amp;", "&")
                   .replace("&gt;", ">")
                   .replace("&lt;", "<");
    }

    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<String>();
        if (text == null || text.isEmpty()) {
            return tokens;
        }
        // Split on one or more non-alphanumeric characters
        String[] parts = decodeEntities(text).split("[^A-Za-z0-9]+");
        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }
            if (containsDigit(part)) {
                continue;               // drops pure numbers and words like "17th", "b52"
            }
            tokens.add(part.toLowerCase());
        }
        return tokens;
    }

    private static boolean containsDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
