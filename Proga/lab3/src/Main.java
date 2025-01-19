import Enums.Gender;
import People.*;
import Things.Dead;
import Things.JasonPhrases;
import Things.Money;
import Exception.CustomJasonPhrasesException;


public class Main {
    public static void main(String[] args) throws CustomJasonPhrasesException {
        Listeners jasonStatham = new Listeners("Джейсон Стетхем", 57, Gender.MALE);
        JasonPhrases jason = new JasonPhrases();
        Semyon semen = new Semyon("Семён", 19, Gender.MALE);
        Father father = new Father("отец", 42, Gender.MALE);
        Mother mother = new Mother("мать", 42, Gender.FEMALE);
        Snake scarapeya = new Snake("Скарапея", 3, Gender.FEMALE, "змея", jason);
        Money money = new Money("деньги");
        Dead dead = new Dead("смерть");
        jasonStatham.addListener(jasonStatham);
        System.out.print(semen.regretIt("последние "));
        System.out.print(money.spent("на меня"));
        scarapeya.pain("Семёна");
        System.out.print(dead.deadCame("пришла"));
        System.out.print(father.fatherInteraction("отец"));
        System.out.println(semen.semyonCame("Пришел"));
        System.out.print(scarapeya.snakeOut());
        System.out.print(mother.motherInteraction("мать"));
        System.out.print(mother.climb("и даже "));
        System.out.println(mother.scold("сына"));
        System.out.print(mother.weaned("язык"));
        System.out.println(scarapeya.snakeChill());
        jasonStatham.removeListener(jasonStatham);
//        System.out.println(semen.equals(mother));
//        System.out.println(semen.hashCode() == mother.hashCode());
//        System.out.println(semen.hashCode());
//        System.out.println(mother.hashCode());
//        try {
//            JasonPhrases jasonPhrases = new JasonPhrases();
//            Snake snake = new Snake("Скарапея", 3, Gender.FEMALE, "змея");
//            snake.personsRole("пойду есть");
//            snake.pain("Семена");
//        } catch (CustomJasonPhrasesException e) {
//            System.out.println("Ошибка: " + e.getMessage());
//        }
    }
}

