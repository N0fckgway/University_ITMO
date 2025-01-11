package People;

import Enums.Gender;

import java.util.Objects;

public abstract class APersons {
    protected String name;
    protected int age;
    protected Gender gender;


    public APersons(){

    }
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;

    }

    public void setGender(Gender gender) {
        this.gender = gender;

    }


    public Gender getGender() {
        return gender;

    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        APersons aPersons = (APersons) o;
        return age == aPersons.age && Objects.equals(name, aPersons.name.toLowerCase()) && gender == aPersons.gender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, gender);
    }

    @Override
    public String toString() {
        return "APersons{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';

    }
}
