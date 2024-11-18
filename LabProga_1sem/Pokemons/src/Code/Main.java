package Code;

import ru.ifmo.se.pokemon.*;
import Code.Pokemons.*;


public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Stantler p1 = new Stantler("Хуй", 1);
        Pokemon p2 = new Pokemon("Пизденка", 2);
        b.addAlly(p1);
        b.addFoe(p2);
        b.go();
    }

}