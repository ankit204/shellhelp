package PreProcessData;
import Classes.*;
import java.util.HashSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StopWordRemover {
	/*
	*  We will use HashSet for stopword lookup
	*  It allows O(1) lookup which is ideal. 
	*/
	HashSet<String> sWords = new HashSet<String>();  
	
	public StopWordRemover( ) throws Exception {
		FileInputStream fis = new FileInputStream(new File(Path.StopwordDir));
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));	
		String line = "";
		while ((line = br.readLine()) != null){
			sWords.add(line.trim());
		}
		br.close();
		fis.close();
	}
	
	public boolean isStopword( char[] word ) {
		String wordStr = new String(word);
		return sWords.contains(wordStr);
	}
	/*
	* Test the implementation
	*/	
	public static void main(String[] args) throws Exception{
		StopWordRemover sr = new StopWordRemover();
		System.out.println(sr.isStopword("be".toCharArray()));
		
	}
}
