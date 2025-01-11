package Things;


import Enums.Gender;

public abstract class Things {
    public String name;
    public Gender gender;
    public Things(String name, Gender gender){
        this.name = name;
        this.gender = gender;
    }

    public String getName(){
        return name;
    }

    public Gender getGender(){
        return gender;
    }

    public abstract void getTrata(String pronoun);

    public abstract String prishlaPrishel();
}
