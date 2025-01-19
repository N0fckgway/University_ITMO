package People;

import Enums.Gender;
import Interfaces.DeadCame;

import java.util.Objects;

public abstract class Persons {
    private String name;
    private int age;
    private Gender gender;


    public Persons(String name, int age, Gender gender) {
        setName(name);
        setAge(age);
        setGender(gender);
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

    public abstract void personsRole(String task) throws Exception;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Persons persons = (Persons) o;
        return getAge() == persons.getAge()
                && Objects.equals(getName(), persons.getName().toLowerCase())
                && Objects.equals(getGender(), persons.getGender());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, gender);
    }

    @Override
    public String toString() {
        return "Persons{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                '}';

    }

    public record PersonsDescribe(String name, int age, Gender gender) {
        @Override
        public String toString() {
            return name + ", " + age + " лет, " + gender.getGender();
        }
    }
}
