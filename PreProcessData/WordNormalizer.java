package PreProcessData;
import Classes.Stemmer;

/**
 * This is for INFSCI 2140 in 2018
 * 
 */
public class WordNormalizer {
	
	public char[] lowercase( char[] chars ) {
		for (int i=0; i < chars.length; i++){
			// normalize by to lower case 
			chars[i] = Character.toLowerCase(chars[i]);
		}
		return chars;
	}
	
	public String stem(char[] chars)
	{
		Stemmer s = new Stemmer();
		s.add(chars, chars.length);	
		s.stem();
		return s.toString();
	}
	
}
