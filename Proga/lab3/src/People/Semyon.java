package People;

import java.lang.Math;

import Enums.Gender;
import Interfaces.RegretIt;
import Interfaces.SemyonCame;
import JasonStatham.JasonPhrases;

public class Semyon extends Persons implements RegretIt, SemyonCame {


    public Semyon(String name, int age, Gender gender) {
        super(name, age, gender);
    }

    @Override
    public String regretIt(String adj) {
        return "- Не жалей, " + getName() + ", что " + adj;

    }

    @Override
    public String semyonCame(String came) {
        return came + " " + getName() + " домой и ";
    }

    @Override
    public void personsRole(String task) {
        System.out.println("Семен исследует новую темку: " + task + ".");
        if (Math.random() <= 0.5) {
            System.out.println("Семен находит лютую темку");

        } else {
            System.out.println("Семен понял, что темка не дает выхлопа");

        }

    }


}
