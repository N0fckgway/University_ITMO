package Enums;

public enum Gender {
    MALE(" "), FEMALE("a "), MIDDLE("ю "), ALL("и ");
    private final String name;

    Gender(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
