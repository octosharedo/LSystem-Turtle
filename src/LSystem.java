import java.util.HashMap;
import java.util.Map;

public class LSystem {

    private static Map<Character,String> rules = new HashMap<Character,String>();

    public static void addRule(Character letter, String rule) {rules.put(letter, rule);}
    public static void clearRules() { rules.clear(); }
    public static String run(String axiom, int iterations)
    {
        String result = axiom;
        for (int i=0; i<iterations;i++) result = convert(result);
        System.out.println(result);
        return result;
    }
    private static String convert(String axiom)
    {
        String result = "";
        for (int i = 0; i < axiom.length(); i++)
        {
            if (rules.containsKey(axiom.charAt(i))) result += rules.get(axiom.charAt(i));
            else result+=axiom.charAt(i);
        }
        return result;
    }

}