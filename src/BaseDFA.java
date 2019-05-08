import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public BaseDFA(int statesCount, int startState, String alphabet, List<Integer> finalStates, List<Map<Integer, Integer>> transitions) {
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
            System.out.println(json.toString());
            
            //read statesCount
            
            statesCount = ((Long) json.get(JSON_KEY_STATES_COUNT)).intValue();
            System.out.println(statesCount);
            
            //read start state
            startState = ((Long) json.get(JSON_KEY_START_STATE)).intValue();
            
            //read alphabet
            alphabet = (String) json.get(JSON_KEY_ALPHABET);
            
            //read final states
            finalStates = new ArrayList<>();
            JSONArray allStates = (JSONArray) json.get(JSON_KEY_FINAL_STATES);
            for (int i = 0; i < allStates.size(); i++) {
                finalStates.add(((Long) allStates.get(i)).intValue());
            }
            
            //read transitions
            JSONArray allTransitions = (JSONArray) json.get(JSON_KEY_TRANSITIONS);
            transitions = new ArrayList<>();
            for (int i = 0; i < allTransitions.size(); i++) {
                
                transitions.add(new HashMap<>());
                
                JSONArray singleStateTransition = (JSONArray) allTransitions.get(i);
                for (Object transition : singleStateTransition) {
                    JSONArray singleTransition = (JSONArray) transition;
                    int charNum = ((Long) singleTransition.get(0)).intValue();
                    int nextState = ((Long) singleTransition.get(1)).intValue();
                    
                    transitions.get(i).put(charNum, nextState);
                }
                
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
        
    }
    
    public void printDetails() {
        System.out.println("states count" + statesCount);
        System.out.println("start state" + startState);
        System.out.println("final states" + finalStates);
        System.out.println("transitions" + transitions);
    }
    
    
}
