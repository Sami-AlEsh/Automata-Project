import java.util.HashMap;
import java.util.Map;

public class Main {
    
    
    public static void main(String[] args) {
    
        BaseDFA dfa = new BaseDFA("C:\\Users\\TareQ\\IdeaProjects\\AutomataProject\\jsonFiles\\temp.json");
        //dfa.printDetails();
    
        System.out.println(dfa.isAccpeted("for"));
        
        
    }
    
}
    
    

