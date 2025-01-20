package Things;


import Interfaces.DeathCame;


public class Death extends Things implements DeathCame {
    public Death(String title) {
        super(title);
    }

    @Override
    public String deathCame(String came) {
        return "\nБез тебя " + came + " бы мне " + getTitle() + ", а теперь я жива, ";

    }

    @Override
    public void use() {
        System.out.println("Смерть - это подведение твоих итогов жизни.");
    }



}
