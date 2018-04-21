import java.io.FileWriter;
import java.util.Map;

import Classes.Path;
import PreProcessData.*;

/**
 * !!! YOU CANNOT CHANGE ANYTHING IN THIS CLASS !!! This is for INFSCI 2140 in
 * 2018
 * 
 */
public class PreProcessRepo{

    public static void main(String[] args) throws Exception {
        long startTime=System.currentTimeMillis(); //star time of running code
        PreProcessRepo ppr = new PreProcessRepo();
        ppr.PreProcess();
        long endTime=System.currentTimeMillis(); //end time of running code
        System.out.println("text corpus running time: "+(endTime-startTime)/60000.0+" min");
    }

    public void PreProcess() throws Exception {
        // Loading the collection file and initiate the DocumentCollection class
        DocumentCollection corpus;
        corpus = new ManCollection();

        // loading stopword list and initiate the StopWordRemover and WordNormalizer class
        StopWordRemover stopwordRemover = new StopWordRemover();
        WordNormalizer normalizer = new WordNormalizer();

        // initiate the BufferedWriter to output result
        FileWriter wr = new FileWriter(Path.ResultPP);

        // initiate a doc object, which can hold document number and document content of a document
        Map<String, String> man = null;

        // process the corpus, document by document, iteractively
        int count=0;
        while ((man = corpus.nextDocument()) != null) {

            for (Map.Entry<String, String> entry : man.entrySet()) {
                String docno = entry.getKey();
                char[] content = entry.getValue().toCharArray();
                if (content.length > 0){
                    // write docno into the result file
                    wr.append(docno + "\n");

                    // initiate the WordTokenizer class
                    WordTokenizer tokenizer = new WordTokenizer(content);

                    // initiate a word object, which can hold a word
                    char[] word = null;

                    // process the document word by word iteratively
                    while ((word = tokenizer.nextWord()) != null) {
                        // each word is transformed into lowercase
                        word = normalizer.lowercase(word);

                        // filter out stopword, and only non-stopword will be written
                        // into result file
                        if (!stopwordRemover.isStopword(word))
                            wr.append(normalizer.stem(word) + " ");
                            //stemmed format of each word is written into result file   
                    }
                    wr.append("\n");// finish processing one document
                    count++;
                    if(count%500==0)
                        System.out.println("finish "+count+" docs");
               }
            }
        }
        System.out.println("totaly document count:  "+count);
        wr.close();
    }
}
