package Things;

import Enums.StoveWear;

import java.util.Objects;

public class Stove extends Things {
    private StoveWear stoveWear;
    public Stove(String name, StoveWear stoveWear){
        super(name);
        setStoveWear(stoveWear);

    }
    public void setStoveWear(StoveWear stoveWear) {
        this.stoveWear = stoveWear;
    }

    public StoveWear getStoveWear(){
        return stoveWear;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Stove stove = (Stove) o;
        return stoveWear == stove.stoveWear;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), stoveWear);
    }

    @Override
    public String toString() {
        return "Stove{" +
                "stoveWear=" + stoveWear +
                '}';
    }

    @Override
    public void use(){
        if (stoveWear == StoveWear.NEW) {
            System.out.println("Печь выглядит как новая и отлично подходит для приготовления пищи или отдыха.");
        } else if (stoveWear == StoveWear.OLD) {
            System.out.println("Старая печь всё ещё функциональна, но её стоит починить.");
        } else {
            System.out.println("Печь сильно повреждена и непригодна для использования.");
        }
    }

}

