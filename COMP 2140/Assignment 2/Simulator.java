public class Simulator {
    public static boolean run (DFA dfa, String input) {
        int inputLength = input.length();

        String[] inputStrings = input.split("");
        String state = dfa.startState;
        String transition;
        
        for (String alphabet : inputStrings) {
            transition = state+"_"+alphabet;
            if (!dfa.transitions.containsKey(transition)) {
                return false;
            }
            state = dfa.transitions.get(transition);
        }
        
        return true;
    }
}