CSCE 5200 - Information Retrieval and Web Search
Project Phase 1: Text Parser
SAI CHARAN NUTHETI
11906470
=====================================================================

LANGUAGE
  Java (written to compile with Java 8 or newer). The parser uses the
  Porter stemmer that was provided with the assignment (Porter.java).

FILES
  TextParser.java      Main program. Runs the whole pipeline and writes the output files.
  TrecReader.java      Splits each TREC file into separate documents (<DOC> ... </DOC>),
                       reading the document name from <DOCNO> and the body from <TEXT>.
  TrecDocument.java    Holds one document (name + body text).
  Tokenizer.java       Splits text on every non-alphanumeric character, drops numbers and
                       any token that contains a digit, and converts tokens to lower case.
  StopWordList.java    Loads stopwordlist.txt and checks whether a token is a stop word.
  WordDictionary.java  Term dictionary: maps every unique stemmed term to a unique ID.
  FileDictionary.java  Document dictionary: maps every document name to a unique ID.
  Porter.java          Porter stemmer (provided with the assignment, unchanged).
  stopwordlist.txt     Stop-word list (provided with the assignment).
  ft911/               TREC data files ft911_1 ... ft911_15 (provided with the assignment).
  parser_output.txt    Required output (see OUTPUT below).
  token_streams.txt    Extra test output: the token stream of every document.

HOW TO RUN
  1. Open a terminal in this folder (the folder holding the .java files, stopwordlist.txt
     and the ft911 folder).
  2. Compile:
         javac *.java
     (Porter.java may print a deprecation note; it is only a warning.)
  3. Run:
         java TextParser
     This uses the defaults: data folder "ft911", stop words "stopwordlist.txt" and
     output "parser_output.txt". Other paths can be given as arguments:
         java TextParser <dataFolder> <stopWordFile> <outputFile>
  It takes a few seconds and prints a summary when it finishes.

PROCESSING STEPS (for each document)
  1. Document separation: each data file holds many documents. Every <DOC> ... </DOC>
     block is one document; its name comes from <DOCNO> and its body from <TEXT>.
     The data files are processed in numeric order (ft911_1, ft911_2, ..., ft911_15),
     so FT911-1 gets document ID 1, FT911-2 gets ID 2, and so on.
  2. Tokenization: the SGML entities &amp; &gt; &lt; are decoded, then the text is split
     on every non-alphanumeric character (spaces, punctuation, hyphens, apostrophes).
     Pure numbers and tokens that contain a digit (e.g. "17th", "1991") are removed,
     and all tokens are converted to lower case.
  3. Stop-word removal: tokens found in stopwordlist.txt are removed.
  4. Stemming: each remaining token is stemmed with the Porter stemmer (Porter.java).
  5. Term selection and dictionaries: every unique stem is added to the term dictionary
     and every document name to the document dictionary.

OUTPUT
  parser_output.txt has two parts, separated by one blank line:
    a) Term dictionary, "term   term ID", sorted alphabetically with IDs 1..N
       in that order, e.g.
           aa                        1
           aaa                       2
    b) Document dictionary, "document name   doc ID", e.g.
           FT911-1                   1
           FT911-2                   2

  token_streams.txt lists, for every document, its ID, its name, the number of tokens,
  and its token stream after stop-word removal and stemming, e.g.
           DocID: 1  DocName: FT911-1  Tokens: 97
           correc publish append articl frank fli shout sir frank whittl ...

RESULTS ON THE PROVIDED DATA
  Documents parsed: 5368
  Tokens kept: 1,036,613
  Unique terms: 32,842

NOTES
  - Only the <TEXT> section of each document is tokenized. The other fields
    (<HEADLINE>, <BYLINE>, <DATE>, <PROFILE>, <PUB>, <PAGE>, ...) are metadata and are
    not indexed.
  - Stop words are removed before stemming, as the preprocessing steps require. A few
    stems can therefore equal a stop word (for example "allowed" stems to "allow");
    these come from words that are not themselves stop words.
