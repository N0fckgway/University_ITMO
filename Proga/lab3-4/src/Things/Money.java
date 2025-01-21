package Things;

import Interfaces.Spent;


public class Money extends Things implements Spent {
    public Money(String name) {
        super(name);
    }

    @Override
    public String spent(String pronoun) {
        return getTitle() + " " + pronoun + " " + "потратил" + ". ";
    }

    @Override
    public void use() {
        System.out.println("Деньги - это всего лишь инструмент, они используются для обеспечения комфорта жизни.");

    }
}
