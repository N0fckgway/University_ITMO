package Interfaces;

public interface Pain {
    default void pain(){
        System.out.print("Змея кусает " + getName("Семена") + " и он орет от боли и умирает.");
    }

    String getName(String name);
}
