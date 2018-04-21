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
    private final Pattern flagPat = Pattern.compile("^.\\w+\\s+([\\\\][\\-])+([\\w\\\\\\-]+)$");
    private final Pattern flagPat2 = Pattern.compile("^.It\\s[Nm\\s]*(([F][l][\\s])+)([\\w\\d\\\\\\-]+)");
    private final Pattern ftag = Pattern.compile("\\\\f[\\w]");
    private final Pattern stag = Pattern.compile("\\\\s[0-9-+]{1,2}");
    private final Pattern mtag = Pattern.compile("\\.[A-Z][A-Za-z]{0,1}\\b");
    private final Pattern fftag = Pattern.compile("\\\\f[\\(\\[][A-Z]{2}");
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

    private String process(String s){
        // remove all "." starting words
        if (s.length() > 0){
            String pLine = ftag.matcher(s).replaceAll("");
            pLine = stag.matcher(pLine).replaceAll("");
            pLine = fftag.matcher(pLine).replaceAll("");
            pLine = mtag.matcher(pLine).replaceAll("");
            return pLine;
        }
        return s;
    }
    
    public Map<String, String> nextDocument() throws IOException {
        if (currIndex == totalCommands){
            return null;
        }
        //System.out.println(listOfCommands[currIndex]);
        String[] nameParts = listOfCommands[currIndex].getName().split(".1");
        String comName = nameParts[0];//listOfCommands[currIndex].getName();
        //System.out.println(" -- Command: "+ comName + " -- ");
        fr = new FileReader(listOfCommands[currIndex]);
        br = new BufferedReader(fr);
        String line = new String();
        String currSection = "";
        boolean descStart = false;
        String content = "";
        Matcher fmatcher, matcher;
        HashMap<String, String> flags = new HashMap<String, String>();
        String currFlag = "";
        
        while ((line = br.readLine()) != null){
            line = line.trim();
            matcher = secStartPat.matcher(line);    
            if (matcher.find()){
                currSection = matcher.group(1).trim();
                //System.out.println(currSection);
                if (currSection.equalsIgnoreCase("DESCRIPTION")){
                    //System.out.println();
                    descStart = true;
                }
                else{
                    if (descStart){
                        descStart = false;
                        currFlag = "";
                    }
                }
                continue;
            }

            // DESCRIPTION rules go here
            if (descStart){
                //System.out.println(line);
                fmatcher = flagPat.matcher(line);
                if (fmatcher.find()){
                    //System.out.println("flag detected: "+fmatcher.group(2));
                    currFlag = comName + "." + fmatcher.group(2);
                    flags.put(currFlag, content);
                    continue;
                }
                fmatcher = flagPat2.matcher(line);
                if (fmatcher.find()){
                    //System.out.println("flag detected: "+fmatcher.group(3));
                    currFlag = comName + "." + fmatcher.group(3);
                    flags.put(currFlag, content);
                    continue;
                }
                if (!currFlag.equals("")){
                    flags.put(currFlag, flags.get(currFlag) + " "+ process(line));
                }
                else{
                    content = content + " " + process(line);
                }
            }

            //Future work: OPTIONS rules go here
        }
        fr.close();
        br.close();
        currIndex++;
        flags.put(comName, content);
        return flags;
    }

    /*
    * Test the text collection implementation
    */  
    public static void main (String[] args) throws IOException{
        ManCollection mc = new ManCollection();
        Map<String, String> result = mc.nextDocument();
        while(result != null){
            for (Map.Entry<String, String> entry : result.entrySet()) {
                System.out.println("---");
                System.out.println(entry.getKey() + ":" + entry.getValue());
                //return;
            }
            result = mc.nextDocument();
        }

    }
}
