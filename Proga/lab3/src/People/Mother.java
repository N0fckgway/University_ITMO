package People;

import Enums.Gender;
import Interfaces.MotherAction;
import Things.Pechka;

public class Mother extends Persons implements MotherAction {
    public Mother(String name, int age, Gender gender) {
        super(name, age, gender);
    }

    @Override
    public void zalezla(String words){
        System.out.print("залезла " + words);
    }

    @Override
    public void pobranity(String son){
        System.out.print("побранить сына не может: ");
    }

    @Override
    public void otnyalsya(String words){
        System.out.print("у нее " + words + " отнялся с испуга. ");
    }
}
