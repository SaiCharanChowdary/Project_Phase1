/** One document from a TREC file: its name (DOCNO) and the body text that gets tokenized. */
public class TrecDocument {
    public final String docNo;
    public final String text;

    public TrecDocument(String docNo, String text) {
        this.docNo = docNo;
        this.text = text;
    }
}
