package ru.suvorov.server.managers;



import lombok.Getter;
import ru.suvorov.model.City;
import ru.suvorov.server.db.DBConnector;
import ru.suvorov.server.db.DBManager;


import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;





public class CollectionManager {
    @Getter
    private final LinkedList<City> collection;
    public HashMap<Long, City> cities = new HashMap<>();
    private final AtomicLong nextId;
    @Getter
    private ZonedDateTime lastInitTime;
    private final Lock lock = new ReentrantLock();
    private final DBManager dbManager;
    private final DBConnector dbConnector;

    public CollectionManager() {
        this.collection = new LinkedList<>();
        this.nextId = new AtomicLong(1);
        this.lastInitTime = null;
        this.dbConnector = new DBConnector();
        this.dbManager = new DBManager(dbConnector);

    }

    public boolean init() {
        lock.lock();
        try {
            DBConnector.initDB();
            return true;
        } catch (Exception e) {
            System.err.println("Ошибка при инициализации коллекции: " + e.getMessage());
            return false;
        } finally {
            lock.unlock();
        }
    }



    public boolean add(City city) {
        lock.lock();
        try {
            city.setId(nextId.getAndIncrement());
            city.setCreationDate(ZonedDateTime.now());
            String username = city.getOwnerUsername();
            dbManager.addElement(city, username);
            return true;
        } finally {
            lock.unlock();
        }
    }


    public ConcurrentLinkedQueue<City> info() {
        lock.lock();
        try {
            setLastInitTime();
            return dbManager.loadCollection();
        } finally {
            lock.unlock();
        }
    }


    public void update(Long id) {
        lock.lock();
        try {
            City city = dbManager.getCityById(id);
            if (city == null) return;
            dbManager.updateCity(id, city);
        } finally {
            lock.unlock();
        }
    }


    public void clearUserObjects(String username) {
        lock.lock();
        try {
            dbManager.clearUserObjects(username);
        } finally {
            lock.unlock();
        }
    }

    public ConcurrentLinkedQueue<City> show() {
        lock.lock();
        try {
            return dbManager.loadCollection();
        } finally {
            lock.unlock();
        }
    }


    public void setLastInitTime() {
        this.lastInitTime = ZonedDateTime.now();
    }

    public Long byId(Long idToUpdate) {
        return new City().getId();
    }
}