public class JavaDFA extends BaseDFA {
    
    final static String jsonFilePath = "C:\\Users\\TareQ\\IdeaProjects\\AutomataProject\\jsonFiles\\java.json";
    private final String NUMBERS = "0123456789";
    
    public JavaDFA() {
        super(jsonFilePath);
    }
    
    @Override
    public int check(String statement) {
        int currentState = startState;
        
        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);
            if (NUMBERS.indexOf(c) >= 0)
                c = '%';
            
            if (!alphabet.contains("" + c)) return -1;
            
            if (currentState == 102 || (currentState == 103 && c != '*')) {
                c = '£'; //£ = any character, 102 and 103 are comments states
            }
            currentState = getNextState(currentState, c);
            if (currentState == -1) return 0;
        }
        
        if (finalStates.containsKey(currentState)) {
            System.out.println(finalStates.get(currentState));
            return 1;
        } else return 0;
    }
    
    
}
