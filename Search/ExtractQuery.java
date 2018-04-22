package Search;

import java.util.ArrayList;
import Classes.Query;
import java.io.*;
import Classes.Path;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashSet;
import Classes.Stemmer;
import java.util.LinkedHashSet;

public class ExtractQuery {
    private ArrayList qList = new ArrayList();
    private final Pattern qid_pattern = Pattern.compile("<num> Number: (.+)");
    private final Pattern title_pattern = Pattern.compile("<title> (.+)");
    private final Pattern desc_pattern = Pattern.compile("<desc>");
    private final Pattern narr_pattern = Pattern.compile("<narr>");
    private int curr_idx = 0; 
    private HashSet<String> sWords = new HashSet<String>();
    private Stemmer stemmer;

    /*
    * Query Extraction module
    */
	public ExtractQuery() throws Exception {
        loadStopWords();
        File f = new File(Path.TopicDir);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        Query q = new Query();
        Matcher matcher;
        String bufferContent = "";
        boolean descStart = false;
        while((line = br.readLine()) != null){
            line = line.trim();
            //System.out.println(line);
            if (line.equals("</top>")){
                q.SetQueryContent(process(bufferContent));
                qList.add(q);
                q = new Query();
                bufferContent = "";
                continue;
            }
            if (q.GetTopicId().equals("")){
                matcher = qid_pattern.matcher(line);
                if (matcher.find()){
                    //System.out.println(matcher.group(1));
                    q.SetTopicId(matcher.group(1).trim());
                }
            }
            else{
                matcher = title_pattern.matcher(line);
                if (matcher.find()){
                     bufferContent = bufferContent + " "+matcher.group(1).trim();
                     continue;
                }
                if (line.contains("<desc>")){
                    descStart = true;
                    continue;
                }
                if (line.contains("<narr>")){
                    descStart = false;
                    continue;
                }
                if (descStart){
                    bufferContent += " " + line; 
                }

            }
        }
	}

    /*
    * Load Stop words in memory. Lookups are O(1)
    */
    private void loadStopWords() throws Exception{
		FileInputStream fis = new FileInputStream(new File(Path.StopwordDir));
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		String line = "";
		while ((line = br.readLine()) != null){
			sWords.add(line.trim());
		}
		br.close();
		fis.close();
    }

    /*
    * Token processing pipeline
    */
    private String processToken(String token){
        token = token.toLowerCase();
        if(sWords.contains(token)){
            return null;
        }
        stemmer = new Stemmer();
        char[] ctoken = token.toCharArray();
        stemmer.add(ctoken, ctoken.length);
        stemmer.stem();
        return stemmer.toString();
    }

    /*
    * Process a query :
    * removes duplicates
    * spits out a string of tokens
    */
    private String process(String q_str){
        String token = "";
        String result = "";
        LinkedHashSet<String> resultSet = new LinkedHashSet<String>();
        for(char c: q_str.toCharArray()){
            if(Character.isLetter(c)){
                token += c;
            }
            else{
                if (token.length() > 0){
                    token = processToken(token);
                    if (token != null){
                        result += " " + token;
                        resultSet.add(token);
                        //System.out.println("token: "+token);
                    }
                    token = "";
                }
            }
        }
        token = processToken(token);
        if (token != null){
            resultSet.add(token);
        }
        return String.join(" ", resultSet);
    }
	
	public boolean hasNext()
	{
		return qList.size() > curr_idx;
	}
	
	public Query next()
	{
		return (Query)qList.get(curr_idx++);
	}
    
    /*
    * test the code
    */
    public static void main(String args[]) throws Exception{
        ExtractQuery eq = new ExtractQuery();
        while(eq.hasNext()){
            Query q = eq.next();
            System.out.println(q.GetTopicId());
            System.out.println(q.GetQueryContent());
        }
    }
}
