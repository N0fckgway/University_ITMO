package People;

import java.lang.Math;
import Exception.CustomJasonPhrasesException;
import Enums.Gender;
import Enums.StoveWear;
import Interfaces.JasonAction;
import Interfaces.Pain;
import Interfaces.SnakeAction;
import Things.JasonPhrases;
import Things.Stove;

public class Snake extends Persons implements Pain, SnakeAction {
    private String animal;
    private JasonPhrases jasonPhrases;


    public Snake(String name, int age, Gender gender, String animal, JasonPhrases jasonPhrases) {
        super(name, age, gender);
        setAnimal(animal);
        setJasonPhrases(jasonPhrases);

    }
    public void setAnimal(String animal){
        this.animal = animal;
    }

    public String getAnimal() {
        return animal;
    }

    public void setJasonPhrases(JasonPhrases jasonPhrases){
        this.jasonPhrases = jasonPhrases;
    }


    @Override
    public void pain(String name) throws CustomJasonPhrasesException {
        if (Math.random() <= 0.2) {
            personsRole("и смотрит на темщика");
            System.out.println("Змея кусает " + name + ", и он орет от боли и умирает. История закончена");
            System.out.println(jasonPhrases.getRandomPhrase());
            System.exit(0);

        } else {
            System.out.print("Я не простая " + getAnimal() + ", a я " + getAnimal() + " " + getName() + ". ");
        }
    }

    @Override
    public String snakeOut() {
        return "выпустил змею " + "из-за пазухи. ";
    }

    @Override
    public String crawlUnder() {
        return "заползла под ";
    }

    @Override
    public String coil() {
        return "свернулась там ";
    }

    @Override
    public String sleep() {
        return "и уснула";
    }

    public String snakeChill() {
        return "Змея же " + getName() + " " + crawlUnder() +
                new Stove("печку", StoveWear.NEW).getName() + ", " + coil() + sleep() + ".";
    }
    @Override
    public void personsRole(String task) throws CustomJasonPhrasesException {
        if (jasonPhrases == null){
            throw new CustomJasonPhrasesException("JasonPhrases не инициализирован для объекта Snake.");
        }
        System.out.println("Змея пробирается сквозь него: " + task + ".");
        System.out.println("Змея говорит: «Ща как кусну, темщик проклятый».");


    }


}
