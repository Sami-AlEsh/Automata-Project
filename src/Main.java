import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    
    
    public static void main(String[] args) {
    
        BaseDFA dfa = new BaseDFA("C:\\Users\\TareQ\\IdeaProjects\\AutomataProject\\jsonFiles\\java.json");
        dfa.printDetails();
    
        //System.out.println(dfa.isAccpeted("for"));
    
    
        while (true) {
            System.out.println(dfa.isAccpeted(new Scanner(System.in).nextLine()));
        }
    }
    
}
    
    

