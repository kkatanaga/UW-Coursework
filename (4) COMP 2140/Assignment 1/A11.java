import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class A11 {
  //check whether the char is a letter
  public enum States {INIT, ID}

  static boolean isLetter(int character) {
    return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
  }
  
  // check whether the char is a letter or digit
  static boolean isLetterOrDigit(int character) {
    return isLetter(character) || (character >= '0' && character <= '9');
  }

  public static Set<String> getIdentifiers(String filename) throws Exception {

		String[] keywordsArray = { "IF", "WRITE", "READ", "RETURN", "BEGIN",
				"END", "MAIN", "INT", "REAL" };

		Set<String> keywords = new HashSet<String>();
		Set<String> identifiers = new HashSet<String>();
		for (String s : keywordsArray) {;
			keywords.add(s);
		}
		States state=States.INIT; // Initially it is in the INIT state. 
		
		StringBuilder code = new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
		while ((line = br.readLine()) != null) { 
			code=code.append(line+"\n");
		} // read the text line by line.
		code =code.append('$'); //add a special symbol to indicate the end of file.  

		int len=code.length();
		String token="";
		boolean quoted = false;

		for (int i=0; i<len; i++) { //look at the characters one by one
			char next_char=code.charAt(i);

			if (next_char == '\"') { //checks for quotation marks
				quoted = !quoted; //start and end of quoted string
			}

			if (quoted == true) { //ignores quoted string
				continue;
			}

			if (state == States.INIT){ 
			    if (isLetter(next_char)){	 
			    	state=States.ID;  // go to the ID state
			    	token=token+next_char;
			    } //ignore everything if it is not a letter
			
			}else if (state == States.ID) {
				if (isLetterOrDigit(next_char)) { //take letter or digit if it is in ID state
				  token=token+next_char;
				} else { // end of ID state
					if (!keywords.contains(token)) { //token is an identifier if it is not a keyword
						identifiers.add(token);
					} //token is a keyword, thus don't add to the identifiers set
					token="";
					state=States.INIT;
				}	

			}

		}
		br.close();
	
		return identifiers;
    }
    
  public static void main(String[] args) throws Exception{
	  final int CASES = 6;
	  ArrayList<Set<String>> caseIDs = new ArrayList<Set<String>>();
	  for (int i = 0; i < CASES; ++i) {
		caseIDs.add(getIdentifiers("A1_Case" + (i+1) + ".tiny"));
		System.out.println("Case " + (i+1) + ":");
		for (String id:caseIDs.get(i)) System.out.println(id);
		System.out.println();
	  }
  }
}