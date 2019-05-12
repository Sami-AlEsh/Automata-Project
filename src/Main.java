import java.util.Scanner;

public class Main {
    
    
    public static void main(String[] args) {
    
        BaseDFA dfa = new BaseDFA("C:\\Users\\TareQ\\IdeaProjects\\AutomataProject\\jsonFiles\\java.json");
        dfa.printDetails();
    
        //System.out.println(dfa.check("for"));
    
    
        Scanner s = new Scanner(System.in).useDelimiter(", ");
        String ssa;
        while ((ssa = s.next()) != null) {
        
            String out = "[" + ssa + ", \"Keyword\"],";
            System.out.println(out);
            
        }
    }
    
}
    
    

