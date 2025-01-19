package Things;


import Interfaces.DeadCame;
import Exception.CustomJasonPhrasesException;

public class Dead extends Things implements DeadCame {
    private JasonPhrases jasonPhrases;
    public Dead(String name) {
        super(name);
    }

    @Override
    public String deadCame(String came) {
        return "\nБез тебя " + came + " бы мне " + getName() + ", а теперь я жива, ";

    }

    @Override
    public void use(String context)  {
        System.out.print("Смерть - это подведение твоих итогов жизни: " + context + ".");
        System.out.print(jasonPhrases.getRandomPhrase());
    }



}
