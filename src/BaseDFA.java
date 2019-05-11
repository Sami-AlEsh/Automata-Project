
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static guru.nidi.graphviz.model.Factory.*;

public class BaseDFA {
    
    private final String JSON_KEY_STATES_COUNT = "statesCount";
    private final String JSON_KEY_ALPHABET = "alphabet";
    private final String JSON_KEY_START_STATE = "startState";
    private final String JSON_KEY_FINAL_STATES = "finalStates";
    private final String JSON_KEY_TRANSITIONS = "transitions";
    
    private int statesCount;
    private int startState;
    private String alphabet;
    private List<Integer> finalStates;
    private List<Map<Integer, Integer>> transitions;
    private Map<Character, Integer> hashOfAlphabet;
    
    public BaseDFA(int statesCount, int startState, String alphabet, List<Integer> finalStates, List<Map<Integer, Integer>> transitions) {
        this.statesCount = statesCount;
        this.startState = startState;
        this.alphabet = alphabet;
        this.finalStates = finalStates;
        this.transitions = transitions;
        
        hashOfAlphabet = new HashMap<>();
        for (int i = 0; i < alphabet.length(); i++) {
            hashOfAlphabet.put(alphabet.charAt(i), i);
        }
    }
    
    public BaseDFA(String jsonFile) {
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile)));
            StringBuilder stringBuilder = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp);
            }
            
            String jsonAsString = new String(stringBuilder);
            JSONObject json = (JSONObject) new JSONParser().parse(jsonAsString);
            
            //read statesCount
            statesCount = ((Long) json.get(JSON_KEY_STATES_COUNT)).intValue();
            
            //read start state
            startState = ((Long) json.get(JSON_KEY_START_STATE)).intValue();
            
            //read alphabet
            alphabet = (String) json.get(JSON_KEY_ALPHABET);
            hashOfAlphabet = new HashMap<>();
            for (int i = 0; i < alphabet.length(); i++) {
                hashOfAlphabet.put(alphabet.charAt(i), i);
            }
            
            //read final states
            finalStates = new ArrayList<>();
            JSONArray allStates = (JSONArray) json.get(JSON_KEY_FINAL_STATES);
            for (int i = 0; i < allStates.size(); i++) {
                finalStates.add(((Long) allStates.get(i)).intValue());
            }
            
            //read transitions
            JSONArray allTransitions = (JSONArray) json.get(JSON_KEY_TRANSITIONS);
            transitions = new ArrayList<>();
            for (int i = 0; i < statesCount; i++) {
                transitions.add(new HashMap<>());
            }
            
            
            for (Object transition : allTransitions) {
                JSONArray singleTransition = (JSONArray) transition;
                int currentState = ((Long) singleTransition.get(0)).intValue();
                String charNum = (String) singleTransition.get(1);
                System.out.println(hashOfAlphabet.get(charNum.charAt(0)));
                int nextState = ((Long) singleTransition.get(2)).intValue();
                
                transitions.get(currentState).put(hashOfAlphabet.get(charNum.charAt(0)), nextState);
            }
            
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("opening json file failed!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error in read json from file!");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    drawAutomata();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        
    }
    
    public void printDetails() {
        System.out.println("states count: " + statesCount);
        System.out.println("start state: " + startState);
        System.out.println("final states: " + finalStates);
        System.out.println("transitions: " + transitions);
        System.out.println("alphabet: " + alphabet);
        System.out.println("hash of alphabet" + hashOfAlphabet);
    }
    
    /*
       this method to check if the statement belong to the language
       @return:
                1 if true
                0 if false
                -1 if there is an char not belong to the alphabet
    */
    public int isAccpeted(String statement) {
        
        int currentState = startState;
        
        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);
            if (hashOfAlphabet.get(c) == null) return -1;
            currentState = getNextState(currentState, c);
            if (currentState == -1) return 0;
        }
        
        if (finalStates.contains(currentState)) return 1;
        else return 0;
    }
    
    private int getNextState(int currentState, char ch) {
        
        int n = hashOfAlphabet.get(ch);
        try {
            int nextState = transitions.get(currentState).get(n);
            return nextState;
        } catch (NullPointerException e) {
            //Dead state
            return -1;
        }
    }
    
    public void drawAutomata() throws IOException {
        
        File automataVisualization = new File("automate Visualization.png");
        automataVisualization.createNewFile();
        Graph automataGraph = graph("Graph: ").directed();
        List<Node> nodes = new ArrayList<>();
        
        for (int i = 0; i < transitions.size(); i++) {
            if (finalStates.contains(i)) {
                nodes.add(node("q" + i).with("shape", "doublecircle").with(Style.FILLED, Color.rgb(255, 200, 50)));
            } else {
                nodes.add(node("q" + i).with("shape", "circle"));
            }
        }
        
        for (int i = 0; i < transitions.size(); i++) {
            
            for (Map.Entry<Integer, Integer> entry : transitions.get(i).entrySet()) {
                String arrowLabel = "" + alphabet.charAt(entry.getKey());
                automataGraph = automataGraph.with(nodes.get(i).link(to(nodes.get(entry.getValue()))
                        .with("label", " " + arrowLabel)));
            }
            
            
        }
        
        Graphviz.fromGraph(automataGraph).width(1900).height(1600).render(Format.PNG).toFile(automataVisualization);
        
        
    }
    
    
}
