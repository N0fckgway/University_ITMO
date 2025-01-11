package Things;

import Enums.Gender;
import Enums.StoveWear;

public class Pechka extends Thing {
    StoveWear stoveWear;
    public Pechka(String name, Gender gender, StoveWear stoveWear){
        super(name, gender);
        this.stoveWear = stoveWear;

    }


}
