import Enums.Gender;
import Enums.StoveWear;
import Interfaces.MotherAction;
import People.Listeners;
import People.Persons;
import People.Semen;
import People.Snake;
import Things.Dead;
import People.Mother;
import Things.Dengi;
import Things.Pechka;

public class Main {
    public static void main(String[] args) {
        Semen semen = new Semen("Семен", 19, Gender.MALE);
        Listeners father = new Listeners("отец", 42, Gender.MALE);
        Listeners mother = new Listeners("мать", 42, Gender.FEMALE);
        Snake scarapeya = new Snake("Скарапея", 3, Gender.FEMALE, "змея");
        Dengi dengi = new Dengi("деньги", Gender.ALL);
        Dead dead = new Dead("смерть", Gender.FEMALE);
        Dead sema = new Dead("Семен", Gender.MALE);
        Mother mom = new Mother("мать", 42, Gender.FEMALE);
        semen.regetIt("последние ");
        dengi.getTrata("на меня");
        scarapeya.bite("не простая");
        dead.prishlaPrishel();
        father.momOrFather();
        sema.prishlaPrishel();
        scarapeya.released();
        mother.momOrFather();
        mom.zalezla("и даже ");
        mom.pobranity("сына");
        mom.otnyalsya("язык");
        System.out.print(scarapeya.getAnimal() + " же " + scarapeya.getName() + " " + scarapeya.zapolzla() +
                new Pechka("печку", Gender.FEMALE, StoveWear.NEW).getName() + ", " + scarapeya.svernulasy() + scarapeya.usnula() + ".");
        System.out.println();


//        System.out.println(semen.equals(mother));
//        System.out.println(semen.hashCode() == mother.hashCode());
//        System.out.println(semen.hashCode());
//        System.out.println(mother.hashCode());


    }
}
