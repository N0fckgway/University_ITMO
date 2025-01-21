package JasonStatham;

import Enums.Phrases;
import Interfaces.JasonAction;

import java.util.Random;

public class JasonPhrases implements JasonAction {
    String[] phrases;
    public JasonPhrases(){
        Phrases[] enumValues = Phrases.values();
        phrases = new String[enumValues.length];

        for (int i = 0; i < enumValues.length; i++){
            phrases[i] = enumValues[i].getPhrase();
        }
    }
    @Override
    public String getRandomPhrase(){
        Random rand = new Random();
        int index = rand.nextInt(phrases.length);
        return phrases[index];
    }

}
