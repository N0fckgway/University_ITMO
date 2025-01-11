package Things;

import Enums.Gender;

public class Thing extends Things{
    String trata = "потратил";
    public Thing(String name, Gender gender){
        super(name, gender);
    }
    @Override
    public String prishlaPrishel(){
        String title = (getGender() == Gender.FEMALE) ? "пришла" : "Пришел";
        if (title.equals("пришла")){
            System.out.print("Без тебя " + title + " бы мне " + getName() + ", а теперь я жива, ");
        }
        else {
            System.out.print(title + " " + getName() + " домой и ");
        }

        return title;
    }
    @Override
    public void getTrata(String pronoun){
        System.out.print(getName() + " " + pronoun + " " + trata + ". ");
    }

    public record rec(String name, Gender gender){
        public String describe() {
            return "Название: " + name + ", Род: " + gender;
        }
    }


}
