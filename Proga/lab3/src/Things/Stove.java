package Things;

import Enums.StoveWear;

public class Stove extends Things {
    private StoveWear stoveWear;
    private JasonPhrases jasonPhrases;
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
    public void use(String context){
        System.out.println("Про печку хз че написать: " + context + ".");
        System.out.println(jasonPhrases.getRandomPhrase());
    }

}
