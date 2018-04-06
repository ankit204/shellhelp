package PreProcessData;
import java.util.ArrayList;

/**
 * This is for INFSCI 2140 in 2018
 * 
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class WordTokenizer {
	ArrayList<String> tokens = new ArrayList<String>();
	
	public WordTokenizer( char[] texts ) {
		String token = new String(); 
		for (final char c: texts){
			// add char to same token if alphanumeric
			if (Character.isLetterOrDigit(c)){
				token = token + c;
			}
			else{
				// check if token is non-empty
				// before adding to tokens array
				if (token.length() > 0){
					tokens.add(token);	
					token = new String(); 
				}
			}	
		}
	}
	
	public char[] nextWord() {
		if (tokens.size() > 0) return tokens.remove(0).toCharArray();
		else return null;
	}
	
}
