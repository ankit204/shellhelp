package Classes;
import java.util.ArrayList;

public class Query {

	private String queryContent;
	private String topicId = "";
    private ArrayList tokenArray;

    public ArrayList tokenize(){
        String word = "";
        tokenArray = new ArrayList();
        for(char c: queryContent.toCharArray()){
            if (c == ' '){
                tokenArray.add(word);
                word = "";
            }
            else{
                word = word + c;
            }
        }
        return tokenArray;
    }
	
	public String GetQueryContent() {
		return queryContent;
	}
	public String GetTopicId() {
		return topicId;
	}
	public void SetQueryContent(String content){
		queryContent=content;
	}	
	public void SetTopicId(String id){
		topicId=id;
	}
}
