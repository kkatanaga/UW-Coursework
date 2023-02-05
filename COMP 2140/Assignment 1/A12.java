import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.*;
import java.util.ArrayList;

public class A12 {
    public enum States {INIT, ID}
    
    public static Set<String> getIdentifiers(String filename) throws Exception {

		String[] keywordsArray = { "IF", "WRITE", "READ", "RETURN", "BEGIN",
				"END", "MAIN", "INT", "REAL" };

		Set<String> keywords = new HashSet<String>();
		Set<String> identifiers = new HashSet<String>();
		for (String s : keywordsArray) {;
			keywords.add(s);
		}
		States state=States.INIT; // Initially it is in the INIT state. 
		
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line;
        Pattern idPattern = Pattern.compile("[a-zA-Z]([a-zA-Z]|[0-9])*");
        Pattern quotedStringPattern = Pattern.compile("\".*\"");
      
		while ((line = br.readLine()) != null) { 
			String unquotedString = quotedStringPattern.matcher(line).replaceAll("");
            Matcher m = idPattern.matcher(unquotedString);
            while (m.find()) {
                String id = unquotedString.substring(m.start(), m.end());
            
                if (!keywords.contains(id)) {
                    identifiers.add(id);
                }
            }
		} // read the text line by line.

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