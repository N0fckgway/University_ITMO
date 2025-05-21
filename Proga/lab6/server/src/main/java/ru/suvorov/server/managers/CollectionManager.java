package ru.suvorov.server.managers;



import lombok.Getter;
import ru.suvorov.model.City;
import ru.suvorov.model.Human;


import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;





public class CollectionManager {
    private final DumpManager dumpManager;
    @Getter
    private final LinkedList<City> collection;
    public HashMap<Long, City> cities = new HashMap<>();
    private final AtomicLong nextId;
    @Getter
    private ZonedDateTime lastInitTime;
    @Getter
    private ZonedDateTime lastSaveTime;

    public CollectionManager(DumpManager dumpManager) {
        this.dumpManager = dumpManager;
        this.collection = new LinkedList<>();
        this.nextId = new AtomicLong(1);
        this.lastInitTime = null;
        this.lastSaveTime = null;
    }

    public boolean init() {
        try {
            List<City> cities = dumpManager.readCollection();
            for (City city : cities) {
                if (city.getId() >= nextId.get()) {
                    nextId.set(city.getId() + 1);
                }
                collection.add(city);
            }
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка при инициализации коллекции: " + e.getMessage());
            return false;
        }
    }

    public boolean save() {
        try {
            dumpManager.writeCollection(new ArrayList<>(collection));
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении коллекции: " + e.getMessage());
            return false;
        }
    }


    public String show() {
        if (collection.isEmpty()) {
            return "Коллекция пуста";
        }
        return collection.stream()
                .map(City::toString)
                .collect(Collectors.joining("\n"));
    }

    public boolean add(City city) {
        city.setId(nextId.getAndIncrement());
        city.setCreationDate(ZonedDateTime.now());
        return collection.add(city);
    }

    public boolean update(long id, City newCity) {
        Optional<City> existingCity = collection.stream()
                .filter(city -> city.getId() == id)
                .findFirst();

        if (existingCity.isPresent()) {
            collection.remove(existingCity.get());
            newCity.setId(id);
            newCity.setCreationDate(existingCity.get().getCreationDate());
            return collection.add(newCity);
        }
        return false;
    }
    public boolean remove(Long id) {
        City city = new City();
        var a = byId(id);
        if (a == null) return false;
        cities.remove(a);
        collection.remove(city.getId());
        update(id, city);
        return true;
    }

    public boolean removeById(long id) {
        return collection.removeIf(city -> city.getId() == id);
    }

    public void clear() {
        collection.clear();
    }


    public boolean removeGreater(City city) {
        return collection.removeIf(c -> c.compareTo(city) > 0);
    }

    public String filterLessThanGovernor(Human governor) {
        return collection.stream()
                .filter(city -> city.getGovernor().compareTo(governor) < 0)
                .map(City::toString)
                .collect(Collectors.joining("\n"));
    }

    public String printAscending() {
        return collection.stream()
                .sorted()
                .map(City::toString)
                .collect(Collectors.joining("\n"));
    }

    public double sumOfMetersAboveSeaLevel() {
        return collection.stream()
                .mapToDouble(City::getMetersAboveSeaLevel)
                .sum();
    }


    public void saveCollection() {
        dumpManager.writeCollection((List<City>) collection);
        lastSaveTime = ZonedDateTime.now();

    }

    public Long byId(Long idToUpdate) {
        return new City().getId();
    }
}