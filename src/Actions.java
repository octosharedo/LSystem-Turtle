import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Stores rules for translating keys into actions */
public class Actions {

    private static Map<Character,Action> rules = new HashMap<Character,Action>();

    public static void addRule(Character key, Action action) {rules.put(key,action);}
    public static void clearRules() {rules.clear();}
    public static void translate(String axiom, int iterations) 
    {
        //get res from LSystem
        String resultingStr = LSystem.run(axiom, iterations);
        //parse it into actions
        ArrayList<Action> commandsToAppend = new ArrayList<>();
        for (int i=0; i<resultingStr.length();i++) 
        {
            if (rules.containsKey(resultingStr.charAt(i)))
            commandsToAppend.add(rules.get(resultingStr.charAt(i)));
        }
        //put actions in the end of commands
        CommandBox.append(commandsToAppend);
    }
    
}