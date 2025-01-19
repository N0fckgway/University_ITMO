package Things;

import Interfaces.Spent;
import Exception.CustomJasonPhrasesException;

public class Money extends Things implements Spent {
    private JasonPhrases jasonPhrases;
    public Money(String name) {
        super(name);
    }

    @Override
    public String spent(String pronoun) {
        return getName() + " " + pronoun + " " + "потратил" + ". ";
    }

    @Override
    public void use(String context) {
        System.out.print("Деньги - это всего лишь инструмент, они используются для обеспечения комфорта жизни: " + context + ".");
        System.out.print(jasonPhrases.getRandomPhrase());
    }
}
