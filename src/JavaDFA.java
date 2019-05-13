public class JavaDFA extends BaseDFA {
    
    final static String jsonFilePath = "jsonFiles\\java.json";
    private final String NUMBERS = "0123456789";
    
    public JavaDFA() {
        super(jsonFilePath);
    }
    
    @Override
    public int check(String statement) {
        int currentState = startState;
        
        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);
            if (!alphabet.contains("" + c)) return -1;
    
            if (currentState == 102 || (currentState == 103 && c != '*') || currentState == 113 || (currentState == 114 && c != '*')) {
                c = '£'; //£ = any character, 102 and 103 are comments states
            }
    
            if (currentState == 106 && Character.isAlphabetic(c)) {
                c = '$';
            }
    
            if (currentState == 107 && (Character.isAlphabetic(c) || Character.isDigit(c))) {
                c = '$';
            }
    
            if (NUMBERS.indexOf(c) >= 0)
                c = '%';
            
            currentState = getNextState(currentState, c);
            if (currentState == -1) return -2;
        }
    
        return currentState;
    }
    
    
}
