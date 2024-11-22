package code;

import ru.ifmo.se.pokemon.*;
import code.pokemons.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        Stantler p1 = new Stantler("Опасный", 90);
        Mudbray p2 = new Mudbray("Жесткий", 72);
        Mudsdale p3 = new Mudsdale("Аккуратный", 78);
        Grubbin p4 = new Grubbin("Капитан", 70);
        Charjabug p5 = new Charjabug("Токсик", 80);
        Vikavolt p6 = new Vikavolt("Энергетическая", 90);
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();

    }
}