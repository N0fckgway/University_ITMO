package People;
import java.lang.Math;
import Enums.Gender;
import Enums.StoveWear;
import Interfaces.Blagodarnosty;
import Interfaces.Looking;
import Interfaces.PrishlaPrishel;
import Things.Pechka;

import java.util.Objects;

public class Persons extends APersons implements Blagodarnosty, Looking {
    public Persons(String name, int age, Gender gender){
        setName(name);
        setAge(age);
        setGender(gender);
    }

    @Override
    public String blagodarnosty(){
        if (Math.random() <= 0.2){
            return "будет бить. ";
        }
        return "отблагодарит. ";

    }

    @Override
    public String lookingSnake(){
        return " увидела змею, ";
    }

    public void momOrFather(){
        String title = (getGender() == Gender.MALE) ? "отец" : "мать";
        if (title.equals("отец")){
            System.out.println("и мой " + title + " тебя " + blagodarnosty());
        }
        else {
            System.out.println("A " + title + " как" + lookingSnake() + "так на " + new Pechka("печку", Gender.FEMALE, StoveWear.NEW).getName());
        }

    }

    public record Humans(String name, int age, Gender gender){
        public String describe() {
            return name + ", " + age + " лет, " + gender.getName();
        }
    }


}
