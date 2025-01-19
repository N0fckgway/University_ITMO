package People;

import Enums.Gender;
import Enums.StoveWear;
import Interfaces.LookingSnake;
import Interfaces.MotherAction;
import Things.JasonPhrases;
import Things.Stove;

public class Mother extends Persons implements MotherAction, LookingSnake {
    private JasonPhrases jasonPhrases;
    public Mother(String name, int age, Gender gender) {
        super(name, age, gender);
    }

    @Override
    public String climb(String words){
        return "залезла " + words;
    }

    @Override
    public String scold(String son){
        return "побранить сына не может: ";
    }

    @Override
    public String weaned(String words){
        return "у нее " + words + " отнялся с испуга. ";
    }

    @Override
    public String lookingSnake(){
        return " увидела змею, ";
    }

    public String motherInteraction(String mother) {
        return "A " + mother + " как" + lookingSnake() + "так на " + new Stove("печку", StoveWear.NEW).getName() + " ";
    }

    @Override
    public void personsRole(String task){
        System.out.println("Мать заботится: " + task + ".");
        System.out.println("Мама говорит: «Любовь и забота делают мир лучше!");
        System.out.println(jasonPhrases.getRandomPhrase());
    }

}
