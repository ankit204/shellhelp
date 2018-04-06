package PreProcessData;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import Classes.Path;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import Classes.Path;

/**
 * This is for INFSCI 2140 in 2018
 *
 */
public class TrecwebCollection implements DocumentCollection {
	private BufferedReader br;
	private FileReader fr;
	private final Pattern tag_pattern = Pattern.compile("<DOCNO>(.+?)</DOCNO>");
	private boolean isProcessed;
	private boolean isTag;
	private String buffer = "";

	public TrecwebCollection() throws IOException {
		File file = new File(Path.DataWebDir); 
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		isProcessed = false;
	}

	
	private String cleanLine(String line) throws IOException{
		/* 
		* Algo: store all chars after < in buffer and
		* move it to content if another < is encountered 
		* before >.
		* Reset buffer if > is encountered after <.
		*/
		String cLine = new String();
		Character c;
		for (int i = 0; i < line.length(); i++){
			c = line.charAt(i);
			if (c == '<'){
				if (isTag){
					cLine = cLine + " " + buffer;
				}
				buffer = "<";
				isTag = true;
				continue;
			}
			if (!isTag){
				cLine += c;		
			}
			else{
				buffer = buffer + c;
			}
			if (isTag && c == '>'){
				isTag = false;
				buffer = "";
			}
		}	
		return cLine;
	}	

	public Map<String, Object> nextDocument() throws IOException {
		if (isProcessed) return null;

		/**
		* Parse <doc> to </doc> content 
		*/
		String line = new String();
		String docno = new String();
		String content = new String();
		boolean numbFound = false;
		boolean contentStart = false;
		isTag = false;
		buffer = "";
		
		while ((line = br.readLine()) != null && !line.trim().equals("</DOC>")){
			line = line.trim();
			// look for document number using regex
			if (!numbFound){
				Matcher matcher = tag_pattern.matcher(line);	
				if (matcher.find()){
					docno = matcher.group(1).trim();
					numbFound = true;
				}
			}
			// update content after removing html tags
			if (contentStart){
				line = cleanLine(line);
				if (line.length() > 0)
					content = content +' '+ line;
			}
			if (!contentStart && line.equals("</DOCHDR>")) contentStart = true;
		}
		
		// close if eof
		if (line == null){
			br.close();
			fr.close();
			isProcessed = true;
			return null;
		}
		
		// return char array
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(docno, content.toCharArray());
		return  result;
	}

	/* 
	* Test the implementation	
	*/
	public static void main (String[] args) throws IOException{
		TrecwebCollection ttc = new TrecwebCollection();
		Map<String, Object> result = ttc.nextDocument();
		while(result != null){
			for (Map.Entry<String, Object> entry : result.entrySet()) {
				char[] entryValChar = (char[]) entry.getValue();
				String entryVal = new String(entryValChar);
				System.out.println(entry.getKey() + ":" + entryVal);
			}
			result = ttc.nextDocument();
		}
	}  
}
