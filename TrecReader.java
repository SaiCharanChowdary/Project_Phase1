import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Splits a TREC data file into its individual documents.
 * Each file holds many <DOC> ... </DOC> blocks; from each block we take the
 * <DOCNO> as the document name and the contents of <TEXT> ... </TEXT> as the body.
 */
public class TrecReader {

    private static final Pattern DOC   = Pattern.compile("<DOC>(.*?)</DOC>", Pattern.DOTALL);
    private static final Pattern DOCNO = Pattern.compile("<DOCNO>\\s*(.*?)\\s*</DOCNO>", Pattern.DOTALL);
    private static final Pattern TEXT  = Pattern.compile("<TEXT>(.*?)</TEXT>", Pattern.DOTALL);

    public List<TrecDocument> read(String path) throws IOException {
        // ISO-8859-1 never fails on odd bytes, so the whole file is always read
        String content = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.ISO_8859_1);
        List<TrecDocument> docs = new ArrayList<TrecDocument>();

        Matcher docMatcher = DOC.matcher(content);
        while (docMatcher.find()) {
            String block = docMatcher.group(1);

            Matcher noMatcher = DOCNO.matcher(block);
            if (!noMatcher.find()) {
                continue;                           // no document name: skip
            }
            String docNo = noMatcher.group(1);

            StringBuilder text = new StringBuilder();
            Matcher textMatcher = TEXT.matcher(block);
            while (textMatcher.find()) {            // join every <TEXT> section, if there is more than one
                text.append(textMatcher.group(1)).append(' ');
            }
            docs.add(new TrecDocument(docNo, text.toString()));
        }
        return docs;
    }
}
