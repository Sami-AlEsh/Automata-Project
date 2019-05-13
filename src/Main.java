import java.util.Scanner;

public class Main {
    
    
    public static void main(String[] args) {
    
        JavaDFA dfa = new JavaDFA();
        dfa.printDetails();
    
        //System.out.println(dfa.check("for"));
    
    
        Scanner s = new Scanner(System.in);
        String ssa;
    
        while ((ssa = s.nextLine()) != null) {
            System.out.println(dfa.getDetails(ssa));
        }
    }
    
}
    
    

