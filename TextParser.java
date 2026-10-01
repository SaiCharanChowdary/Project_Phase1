import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * CSCE 5200 - Project Phase 1: Text Parser.
 *
 * Pipeline for every document:
 *   1. split the TREC files into documents (TrecReader)
 *   2. tokenize: split on non-alphanumerics, drop tokens with digits, lower-case (Tokenizer)
 *   3. remove stop words (StopWordList)
 *   4. stem the remaining words with the Porter stemmer (Porter)
 *   5. add terms to the WordDictionary and the document to the FileDictionary
 *
 * Output:
 *   parser_output.txt  - term dictionary (term : term ID) followed by document dictionary (doc name : doc ID)
 *   token_streams.txt  - for each document, its ID, name and the token stream after
 *                        stop-word removal and stemming (to check that parsing is correct)
 *
 * Usage: java TextParser [dataDir] [stopWordFile] [outputFile]
 *   defaults: ft911  stopwordlist.txt  parser_output.txt
 */
public class TextParser {

    private final Tokenizer tokenizer = new Tokenizer();
    private final Porter stemmer = new Porter();
    private final StopWordList stopWords;
    private final WordDictionary wordDictionary = new WordDictionary();
    private final FileDictionary fileDictionary = new FileDictionary();

    public TextParser(StopWordList stopWords) {
        this.stopWords = stopWords;
    }

    /** Full preprocessing of one piece of text: tokenize, remove stop words, stem. */
    public List<String> process(String text) {
        List<String> terms = new ArrayList<String>();
        for (String token : tokenizer.tokenize(text)) {
            if (stopWords.isStopWord(token)) {
                continue;
            }
            String stem = stemmer.stripAffixes(token);
            if (stem == null || stem.isEmpty()) {
                continue;
            }
            terms.add(stem);
        }
        return terms;
    }

    /** Data files sorted by their numeric suffix (ft911_1, ft911_2, ..., ft911_15). */
    private static File[] sortedDataFiles(File dir) {
        File[] files = dir.listFiles(new java.io.FileFilter() {
            public boolean accept(File f) {
                return f.isFile() && !f.getName().startsWith(".");
            }
        });
        if (files == null) {
            return new File[0];
        }
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File a, File b) {
                int c = Long.compare(suffixNumber(a.getName()), suffixNumber(b.getName()));
                return c != 0 ? c : a.getName().compareTo(b.getName());
            }
        });
        return files;
    }

    private static long suffixNumber(String name) {
        String digits = name.replaceAll("^.*?(\\d+)$", "$1");
        try {
            return Long.parseLong(digits);
        } catch (NumberFormatException e) {
            return Long.MAX_VALUE;
        }
    }

    public void run(String dataDir, String outputFile, String streamFile) throws IOException {
        File dir = new File(dataDir);
        if (!dir.isDirectory()) {
            throw new IOException("Data directory not found: " + dataDir);
        }

        TrecReader reader = new TrecReader();
        BufferedWriter streams = new BufferedWriter(new FileWriter(streamFile));
        long totalTokens = 0;

        try {
            for (File file : sortedDataFiles(dir)) {
                List<TrecDocument> docs = reader.read(file.getPath());
                System.out.println("Parsing " + file.getName() + " (" + docs.size() + " documents)");

                for (TrecDocument doc : docs) {
                    int docId = fileDictionary.add(doc.docNo);
                    List<String> terms = process(doc.text);
                    totalTokens += terms.size();

                    streams.write("DocID: " + docId + "  DocName: " + doc.docNo
                            + "  Tokens: " + terms.size());
                    streams.newLine();
                    StringBuilder line = new StringBuilder();
                    for (String term : terms) {
                        wordDictionary.add(term);
                        line.append(term).append(' ');
                    }
                    streams.write(line.toString().trim());
                    streams.newLine();
                    streams.newLine();
                }
            }
        } finally {
            streams.close();
        }

        wordDictionary.assignIds();
        writeDictionaries(outputFile);

        System.out.println();
        System.out.println("Documents parsed : " + fileDictionary.size());
        System.out.println("Tokens kept      : " + totalTokens);
        System.out.println("Unique terms     : " + wordDictionary.size());
        System.out.println("Output written to " + outputFile + " and " + streamFile);
    }

    private void writeDictionaries(String outputFile) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
        try {
            for (Map.Entry<String, Integer> e : wordDictionary.entries().entrySet()) {
                out.write(String.format("%-25s %d", e.getKey(), e.getValue()));
                out.newLine();
            }
            out.newLine();
            for (Map.Entry<String, Integer> e : fileDictionary.entries().entrySet()) {
                out.write(String.format("%-25s %d", e.getKey(), e.getValue()));
                out.newLine();
            }
        } finally {
            out.close();
        }
    }

    public static void main(String[] args) {
        String dataDir    = args.length > 0 ? args[0] : "ft911";
        String stopFile   = args.length > 1 ? args[1] : "stopwordlist.txt";
        String outputFile = args.length > 2 ? args[2] : "parser_output.txt";
        String streamFile = "token_streams.txt";

        try {
            StopWordList stopWords = new StopWordList(stopFile);
            System.out.println("Loaded " + stopWords.size() + " stop words from " + stopFile);
            new TextParser(stopWords).run(dataDir, outputFile, streamFile);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
