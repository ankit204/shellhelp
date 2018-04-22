package Indexing;

import java.io.IOException;
import java.util.Map;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import Classes.Path;
import java.util.HashMap;

public class PreProcessedCorpusReader {
	private BufferedReader br;
	private FileReader fr;
	
	public PreProcessedCorpusReader() throws IOException {
		File file = new File(Path.ResultPP);
		fr = new FileReader(file);
		br = new BufferedReader(fr);
	}

	public Map<String, Object> NextDocument() throws IOException {
		String line = "";
		String docno = "";
		docno = br.readLine();
		if (docno == null){
			br.close();
			fr.close();
			return null;
		}
		docno = docno.trim();
		line = br.readLine().trim();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(docno, line.toCharArray());
		return result;
	}

	public static void main(String[] args) throws IOException{
		PreProcessedCorpusReader pcr;
		pcr = new PreProcessedCorpusReader();
		Map<String, Object> doc = null;
		while((doc = pcr.NextDocument()) != null){
			String docno = doc.keySet().iterator().next();
			char[] content = (char[]) doc.get(docno);
			System.out.println(docno);
		}
	}

}
