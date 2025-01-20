package Listeners;

import Enums.Gender;

public record DataListener(String listenerName, int listenerAge, Gender listenerGender) {
    @Override
    public String toString(){
        return String.format("Имя слушателя: %s, Возраст слушателя: %d, Пол слушателя: %s", listenerName, listenerAge, listenerGender);
    }
}
