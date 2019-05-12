
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
    
    protected final String JSON_KEY_STATES_COUNT = "statesCount";
    protected final String JSON_KEY_ALPHABET = "alphabet";
    protected final String JSON_KEY_START_STATE = "startState";
    protected final String JSON_KEY_FINAL_STATES = "finalStates";
    protected final String JSON_KEY_TRANSITIONS = "transitions";
    
    protected int statesCount;
    protected int startState;
    protected String alphabet;
    protected Map<Integer, String> finalStates;
    protected List<Map<Character, Integer>> transitions;
    
    public BaseDFA(int statesCount, int startState, String alphabet, Map<Integer, String> finalStates, List<Map<Character, Integer>> transitions) {
        this.statesCount = statesCount;
        this.startState = startState;
        this.alphabet = alphabet;
        this.finalStates = finalStates;
        this.transitions = transitions;
        
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
            
            //read final states
            finalStates = new HashMap<>();
            JSONArray allStates = (JSONArray) json.get(JSON_KEY_FINAL_STATES);
            for (int i = 0; i < allStates.size(); i++) {
                JSONArray singlFinalState = (JSONArray) allStates.get(i);
                int number = ((Long) singlFinalState.get(0)).intValue();
                String dif = (String) singlFinalState.get(1);
    
                finalStates.put(number, dif);
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
                int nextState = ((Long) singleTransition.get(2)).intValue();
    
                transitions.get(currentState).put(charNum.charAt(0), nextState);
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
        //System.out.println("hash of alphabet" + hashOfAlphabet);
    }
    
    /*
       this method to check if the statement belong to the language
       @return:
                1 if true
                0 if false
                -1 if there is an char not belong to the alphabet
    */
    public int check(String statement) {
        
        int currentState = startState;
        
        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);
            if (!alphabet.contains("" + c)) return -1;
    
            currentState = getNextState(currentState, c);
            if (currentState == -1) return 0;
        }
    
        if (finalStates.containsKey(currentState)) {
            System.out.println(finalStates.get(currentState));
            return 1;
        }
        else return 0;
    }
    
    protected int getNextState(int currentState, char ch) {
        
        try {
            int nextState = transitions.get(currentState).get(ch);
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
            if (finalStates.containsKey(i)) {
                nodes.add(node("q" + i + "\n" + finalStates.get(i)).with("shape", "doublecircle").with(Style.FILLED, Color.rgb(255, 200, 50)));
            } else {
                nodes.add(node("q" + i).with("shape", "circle"));
            }
        }
        
        for (int i = 0; i < transitions.size(); i++) {
    
            for (Map.Entry<Character, Integer> entry : transitions.get(i).entrySet()) {
                String arrowLabel = "" + entry.getKey();
                automataGraph = automataGraph.with(nodes.get(i).link(to(nodes.get(entry.getValue()))
                        .with("label", " " + arrowLabel)));
            }
            
            
        }
        
        Graphviz.fromGraph(automataGraph).width(1900).height(1600).render(Format.PNG).toFile(automataVisualization);
    }
    
    public String getDetails(String statment) {
        int checkValue = check(statment);
        String out = "";
        if (checkValue == -1) {
            out = "There is strange character!";
        }
        
        return out;
    }
    
    
}
