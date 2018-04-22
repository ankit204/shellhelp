
import java.util.Map;
import Indexing.*;

public class Index {
	public static void main(String[] args) throws Exception {
		Index ind = new Index();
		long startTime = System.currentTimeMillis();
		ind.WriteIndex();
		long endTime = System.currentTimeMillis();
		System.out.println("index web corpus running time: "+(endTime-startTime)/60000.0+" min");
		startTime=System.currentTimeMillis();
		ind.ReadIndex("copi");
		endTime=System.currentTimeMillis();
		System.out.println("load index & retrieve running time: "+(endTime-startTime)/60000.0+" min");
	}

	public void WriteIndex() throws Exception {
		// Initiate pre-processed collection file reader
		PreProcessedCorpusReader corpus = new PreProcessedCorpusReader();
		
		// initiate the output object
		MyIndexWriter output = new MyIndexWriter();
		
		// initiate a doc object, which will hold document number and document content
		Map<String, Object> doc = null;

		int count=0;
		// build index of corpus document by document
		while ((doc = corpus.NextDocument()) != null) {
			// load document number and content of the document
			String docno = doc.keySet().iterator().next();
			char[] content = (char[]) doc.get(docno);		
			
			// index this document
			output.index(docno, content); 
			
			count++;
			if(count%500==0)
				System.out.println("finish "+count+" docs");
		}
		System.out.println("\ttotal document count:  "+count);
		output.close();
	}
	
	public void ReadIndex(String token) throws Exception {
		// Initiate the index file reader
		MyIndexReader ixreader = new MyIndexReader();
		
		// conduct retrieval
		int df = ixreader.DocFreq(token);
		long ctf = ixreader.CollectionFreq(token);
		System.out.println(" >> the token \""+token+"\" appeared in "+df+" documents and "+ctf+" times in total");
		if(df>0){
			int[][] posting = ixreader.getPostingList(token);
			for(int ix=0;ix<posting.length;ix++){
				int docid = posting[ix][0];
				int freq = posting[ix][1];
				String docno = ixreader.getDocno(docid);
				System.out.printf("    %20s    %6d    %6d\n", docno, docid, freq);
			}
		}
		ixreader.close();
	}
}
