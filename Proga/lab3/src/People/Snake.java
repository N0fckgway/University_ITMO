package People;

import java.lang.Math;
import java.util.Objects;

import Exception.CustomSetDeathException;
import Enums.Gender;
import Enums.StoveWear;
import Interfaces.Pain;
import Interfaces.SnakeAction;
import JasonStatham.JasonPhrases;
import Things.Stove;

public class Snake extends Persons implements Pain, SnakeAction {
    private String animal;
    private JasonPhrases jasonPhrases;


    public Snake(String name, int age, Gender gender, String animal, JasonPhrases jasonPhrases) {
        super(name, age, gender);
        setAnimal(animal);
        setJasonPhrases(jasonPhrases);

    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Snake snake = (Snake) o;
        return Objects.equals(animal, snake.animal) && Objects.equals(jasonPhrases, snake.jasonPhrases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), animal, jasonPhrases);
    }

    @Override
    public String toString() {
        return "Snake{" +
                "animal='" + animal + '\'' +
                ", jasonPhrases=" + jasonPhrases +
                '}';
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
    public void pain(Persons person) throws CustomSetDeathException {
        if (Math.random() <= 0.2) {
            personsRole("и смотрит на темщика");
            System.out.println("Змея кусает " + person.getName() + "a" + ", и он орет от боли и умирает. История закончена");
            person.setDeath();
            System.out.println(jasonPhrases.getRandomPhrase());
            System.exit(0);
        }
        else {
            System.out.print("Я не простая " + getAnimal() + ", a я " + getAnimal() + " " + getName() + ". ");
        }
        try{
            isDeath();
        }
        catch (CustomSetDeathException e){
            personsRole("и смотрит на темщика");
            System.out.println("Змея кусает " + person.getName() + "a" + ", и он орет от боли и умирает. История закончена");
            System.out.println(jasonPhrases.getRandomPhrase());
            System.exit(0);
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
                new Stove("печку", StoveWear.NEW).getTitle() + ", " + coil() + sleep() + ".";
    }
    @Override
    public void personsRole(String task) {
        System.out.println("Змея пробирается сквозь него: " + task + ".");
        System.out.println("Змея говорит: «Ща как кусну, темщик проклятый».");
    }


}
