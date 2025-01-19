package People;

import Enums.Gender;
import Interfaces.GetThanks;
import Things.JasonPhrases;

public class Father extends Persons implements GetThanks {
    private JasonPhrases jasonPhrases;
    public Father(String name, int age, Gender gender){
        super(name, age, gender);
    }

    @Override
    public String getThanks(){
        if (Math.random() <= 0.2){
            return "будет бить. ";
        }
        return "отблагодарит. ";

    }
    public String fatherInteraction(String father) {
        return "и мой " + father + " тебя " + getThanks();
    }

    @Override
    public void personsRole(String task){
        System.out.println("Отец работает: " + task + ".");
        System.out.println("Отец говорит: «Упорный труд - ключ к успеху!»");
        System.out.println(jasonPhrases.getRandomPhrase());
    }


}
