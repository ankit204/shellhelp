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
import java.util.logging.*;

public class ManCollection implements DocumentCollection {
	private BufferedReader br;
	private FileReader fr;
	private final Pattern secStartPat = Pattern.compile("^.sh \"?([^\"]+)\"?", Pattern.CASE_INSENSITIVE);
	private boolean isProcessed;
	private static final Logger logger = Logger.getLogger(ManCollection.class.getName());
	private int currIndex;
	private int totalCommands;
	private File[] listOfCommands;

	public ManCollection() throws IOException {
		File dir = new File(Path.ManDir); 
		listOfCommands = dir.listFiles();
		currIndex = 0;
		totalCommands = listOfCommands.length;		
		System.out.println("Total commands to index : "+totalCommands);
	}
	
	public Map<String, Object> nextDocument() throws IOException {
		if (currIndex == totalCommands){
			return null;
		}
		String nameParts = listOfCommands[currIndex].split(".1");
		String comName = nameParts[0];//listOfCommands[currIndex].getName();
		//logger.info("Command :"+ comName);
		System.out.println(" -- Command: "+ comName + " -- ");
		fr = new FileReader(listOfCommands[currIndex]);
		br = new BufferedReader(fr);
		String line = new String();
		boolean nameFound = false;
		boolean nameStart = false;
		String currSection = "";
		
		while ((line = br.readLine()) != null){
			line = line.trim();
			
			Matcher matcher = secStartPat.matcher(line);	
			if (matcher.find()){
				currSection = matcher.group(1).trim();
				System.out.println(currSection);
				nameStart = true;
				continue;
			}
			if (currSection.equalsIgnoreCase("name")){
				name = line;
			}
			
			fr.close();
			br.close();
			currIndex++;
			return new HashMap<String, Object>();
	}

	/*
	* Test the text collection implementation
	*/	
	public static void main (String[] args) throws IOException{
		ManCollection mc = new ManCollection();
		Map<String, Object> result = mc.nextDocument();
		while(result != null){
			/*for (Map.Entry<String, Object> entry : result.entrySet()) {
				System.out.println(entry.getKey() + ":" + entry.getValue().toString());
				return;
			}*/
			result = mc.nextDocument();
		}

	}
}
