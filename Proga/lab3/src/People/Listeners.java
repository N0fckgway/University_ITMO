package People;

import Enums.Gender;
import Interfaces.ReceiveMessage;

import java.util.ArrayList;
import java.util.Objects;


public class Listeners extends Persons implements ReceiveMessage {
    private final ArrayList<Listeners> listeners = new ArrayList<>();

    public Listeners(String name, int age, Gender gender) {
        super(name, age, gender);
    }

    public void addListener(Listeners listener) {
        listeners.add(listener);
        System.out.println("Добавлен слушатель: " + listener.getName());
    }

    public void removeListener(Listeners listener) {
        listeners.remove(listener);
        System.out.println("Слушатель удален: "  + listener.getName());

    }

    public void notifyListeners(String message){
        for (Listeners listener: listeners){
            listener.receiveMessage(message);
        }
    }


    @Override
    public void receiveMessage(String message) {
        System.out.println(super.getName() + " получил сообщение: " + message);
    }

    public int cntListener(){
        return listeners.size();
    }


    public Listeners findListener(String name){
        for (Listeners listener: listeners){
            if (listener.getName().equals(name.toLowerCase())){
                return listener;
            }
        }
        return null;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Listeners listeners1 = (Listeners) o;
        return Objects.equals(listeners, listeners1.listeners);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listeners);
    }

    @Override
    public String toString() {
        return "Listeners{" +
                "listeners=" + listeners +
                '}';
    }
}
