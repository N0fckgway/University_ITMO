package Listeners;

import Enums.Gender;
import Enums.StoveWear;
import Interfaces.ReceiveMessage;
import Things.Stove;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Objects;


public class Listeners implements ReceiveMessage {
    protected ArrayList<Listeners> listeners = new ArrayList<>();
    protected String listenerName;
    protected int listenerAge;
    protected Gender listenerGender;

    public Listeners(String listenerName, int listenerAge, Gender listenerGender) {
        this.listenerName = listenerName;
        this.listenerAge = listenerAge;
        this.listenerGender = listenerGender;
    }

    public String getListenerName() {
        return listenerName;
    }



    public void addListener(Listeners listener) {
        listeners.add(listener);
        System.out.println("Добавлен слушатель: " + getListenerName());
    }

    public void removeListener(Listeners listener) {
        listeners.remove(listener);
        System.out.println("Слушатель удален: "  + getListenerName());

    }

    public void messageFromListeners(String message){
        for (Listeners listener: listeners){
            listener.receiveMessage(message, new Stove("печь", StoveWear.NEW));
        }
    }


    @Override
    public void receiveMessage(String message, Stove stove) {
        if (Math.random() <= 0.3){
            System.out.println(getListenerName() + " использует печку и делает отзыв по ней: ");
            stove.use();
        }
        else
            System.out.println(getListenerName() + " отправил сообщение: " + message);
    }

    public int cntListener(){
        return listeners.size();
    }


    public Listeners findListener(String name){
        for (Listeners listener: listeners){
            if (getListenerName().equals(name.toLowerCase())){
                return listener;
            }
        }
        return null;

    }



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Listeners listeners1 = (Listeners) o;
        return listenerAge == listeners1.listenerAge &&
                Objects.equals(listeners, listeners1.listeners) &&
                Objects.equals(listenerName, listeners1.listenerName) &&
                listenerGender == listeners1.listenerGender;
    }

    @Override
    public int hashCode() {
        return Objects.hash(listeners, listenerName, listenerAge, listenerGender);
    }

    @Override
    public String toString() {
        return "Listeners{" +
                "listeners=" + listeners +
                ", listenerName='" + listenerName + '\'' +
                ", listenerAge=" + listenerAge +
                ", listenerGender=" + listenerGender +
                '}';
    }
}
