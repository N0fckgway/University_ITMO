package ru.suvorov.server.managers;

import ru.suvorov.server.builder.ConcreteCityBuilder;
import ru.suvorov.server.builder.DirectorBuilder;
import ru.suvorov.server.collection.model.City;
import ru.suvorov.server.util.CollectionElement;


import java.time.ZonedDateTime;
import java.util.*;

public class CollectionManager {
    public Long currentId = 1L;
    public Set<Long> existingId = new HashSet<>();
    public HashMap<Long, City> city = new HashMap<>();
    private final LinkedList<City> collection = new LinkedList<>();
    private ZonedDateTime lastInitTime;
    private ZonedDateTime lastSaveTime;
    private final DumpManager dumpManager;



    public LinkedList<City> getCollection() {
        return collection;
    }

    public ZonedDateTime getLastInitTime() {
        return lastInitTime;
    }

    public ZonedDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public DumpManager getDumpManager() {
        return dumpManager;
    }

    public CollectionManager(DumpManager dumpManager) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.dumpManager = dumpManager;

    }

    public City byId(Long id) {
        return city.get(id);
    }

    public boolean isContain(City collection) {
        return collection == null || byId(collection.getId()) != null;

    }

    public CollectionElement getCollectionElement() {
        ConcreteCityBuilder builder = new ConcreteCityBuilder();
        return DirectorBuilder.constructCityBuilder(builder);


    }

    public boolean add(City c) {
        if (isContain(c)) return false;
        city.put(c.getId(), c);
        collection.add(c);
        update();
        return true;
    }

    public boolean update(City c) {
        if (!(isContain(c))) return false;
        collection.remove(byId(c.getId()));
        city.put(c.getId(), c);
        collection.add(c);
        update();
        return true;
    }

    public boolean remove(Long id) {
        var a = byId(id);
        if (a == null) return false;
        city.remove(a.getId());
        collection.remove(a);
        update();
        return true;
    }

    public void update() {
        Collections.sort(collection);

    }

    public boolean init() {
        collection.clear();
        city.clear();
        dumpManager.readCollection(collection);
        lastInitTime = ZonedDateTime.now();
        for (var e : collection)
            if (byId(e.getId()) != null) {
                collection.clear();
                city.clear();
                return false;
            } else {
                if (e.getId() > currentId) currentId = e.getId();
                city.put(e.getId(), e);
            }
        update();
        return true;
    }

    public void saveCollection() {
        dumpManager.writeCollection(collection);
        lastSaveTime = ZonedDateTime.now();

    }



    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста";

        StringBuilder information = new StringBuilder();
        for (var city : collection) {
            information.append(city).append("\n\n");
        }
        return information.toString().trim();
    }



}