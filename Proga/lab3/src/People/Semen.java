package People;

import Enums.Gender;
import Interfaces.RegetIt;

public class Semen extends Persons implements RegetIt {
    public Semen(String name, int age, Gender gender) {
        super(name, age, gender);
    }

    @Override
    public void regetIt(String adj) {
        System.out.print("- Не жалей, " + getName() + ", что " + adj);

    }
}
