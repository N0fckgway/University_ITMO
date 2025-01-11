package People;
import java.lang.Math;
import Enums.Gender;
import Interfaces.Pain;
import Interfaces.Released;
import Interfaces.SnakeAction;
import Things.Pechka;

public class Snake extends Persons implements Pain, Released, SnakeAction {
    public String animal;
    public Snake(String name, int age, Gender gender, String animal){
        super(name, age, gender);
        this.animal = animal;

    }

    @Override
    public String getName(String name){
        super.name = name;
        return super.name;
    }

    public String getAnimal(){
        return animal;
    }

    public void bite(String adj){
        if (Math.random() <= 0.2) {
            pain();
            System.out.println(" История закончена");
            System.exit(0);
        }
        else {
            System.out.println("Я " + adj + " " + animal + ", a я " + animal + " " + getName() + ".");
        }
    }

    @Override
    public void released(){
        System.out.print("выпустил змею " + "из-за пазухи. ");
    }

    @Override
    public String zapolzla(){
        return "заползла под ";
    }

    @Override
    public String svernulasy() {
        return "свернулась там ";
    }

    @Override
    public String usnula(){
        return "и уснула";
    }


}
