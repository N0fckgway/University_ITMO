package ru.suvorov.server.db;

import ru.suvorov.enums.Climate;
import ru.suvorov.enums.Government;
import ru.suvorov.enums.StandardOfLiving;
import ru.suvorov.model.City;
import ru.suvorov.model.Coordinates;
import ru.suvorov.model.Human;
import ru.suvorov.server.exception.DataBaseException;
import ru.suvorov.server.util.Sha_512;

import java.beans.Transient;
import java.sql.*;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.List;

public class DBManager {
    private final DBConnector dbConnector;

    public DBManager(DBConnector dbConnector) {
        this.dbConnector = dbConnector;

    }


    public ConcurrentLinkedQueue<City> loadCollection() throws DataBaseException {
        return dbConnector.handleQuery((Connection conn) -> {
            String selectCity = "SELECT c.*, u.login as owner_username, h.name as governor_name, h.age, h.height, h.birthday FROM cities c JOIN users u ON c.owner_id = u.id JOIN humans h ON c.human = h.id";
            Statement statementCities = conn.createStatement();
            ResultSet collectionSet = statementCities.executeQuery(selectCity);
            ConcurrentLinkedQueue<City> resDeque = new ConcurrentLinkedQueue<>();

            while (collectionSet.next()) {
                Human governor = new Human (
                        collectionSet.getString("governor_name"),
                        collectionSet.getInt("age"),
                        collectionSet.getFloat("height"),
                        collectionSet.getTimestamp("birthday").toInstant().atZone(ZoneId.systemDefault())
                );

                Coordinates coordinates = new Coordinates(
                        collectionSet.getLong("x"),
                        collectionSet.getLong("y")
                );
                
                Government government = Government.valueOf(collectionSet.getString("government"));
                Climate climate = Climate.valueOf(collectionSet.getString("climate"));
                StandardOfLiving standardOfLiving = StandardOfLiving.valueOf(collectionSet.getString("standardofliving"));
                
                City city = new City(
                        collectionSet.getString("name"),
                        coordinates,
                        collectionSet.getInt("area"),
                        collectionSet.getLong("population"),
                        collectionSet.getDouble("metersabovesealevel"),
                        collectionSet.getString("capital"),
                        climate,
                        government,
                        standardOfLiving,
                        governor

                );
                city.setId(collectionSet.getLong("id"));
                city.setOwnerUsername(collectionSet.getString("owner_username"));
                resDeque.add(city);
            }
            return resDeque;

        }
        );


    }

    public Long addElement(City city, String username) {
        return dbConnector.handleQuery((Connection conn) -> {
            String insertHuman = "INSERT INTO humans(name, age, height, birthday) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatements = conn.prepareStatement(insertHuman, Statement.RETURN_GENERATED_KEYS);
            preparedStatements.setString(1, city.getGovernor().getName());
            preparedStatements.setLong(2, city.getGovernor().getAge());
            preparedStatements.setFloat(3, city.getGovernor().getHeight());
            preparedStatements.setDate(4, Date.valueOf(city.getGovernor().getBirthday().toLocalDate()));
            preparedStatements.executeUpdate();
            ResultSet resHuman = preparedStatements.getGeneratedKeys();
            if (!resHuman.next()) {
                throw new SQLException("Не удалось получить id человека");
            }
            long id_human = resHuman.getLong(1);


            String insertCoordinates = "INSERT INTO coordinates(x, y) VALUES (?, ?)";
            PreparedStatement prepared = conn.prepareStatement(insertCoordinates, Statement.RETURN_GENERATED_KEYS);
            prepared.setLong(1, city.getCoordinates().getX());
            prepared.setLong(2, city.getCoordinates().getY());
            prepared.executeUpdate();

            String userIdQuery = "SELECT id FROM users WHERE login = ?";
            PreparedStatement userStmt = conn.prepareStatement(userIdQuery);
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();
            if (!userRs.next()) {
                throw new SQLException("Пользователь не найден");
            }
            long id_user = userRs.getLong(1);




            String addElementCity = "INSERT INTO cities "
                    + "(name, x, y, creation_date, area, population, metersabovesealevel, capital, climate, government, standardofliving, human, owner_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(addElementCity, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, city.getName());
            preparedStatement.setLong(2, city.getCoordinates().getX());
            preparedStatement.setLong(3, city.getCoordinates().getY());
            preparedStatement.setDate(4, Date.valueOf(city.getCreationDate().toLocalDate()));
            preparedStatement.setInt(5, city.getArea());
            preparedStatement.setLong(6, city.getPopulation());
            preparedStatement.setDouble(7, city.getMetersAboveSeaLevel());
            preparedStatement.setString(8, city.getCapital());
            preparedStatement.setString(9, city.getClimate().toString());
            preparedStatement.setString(10, city.getGovernment().toString());
            preparedStatement.setString(11, city.getStandardOfLiving().toString());
            preparedStatement.setLong(12, id_human);
            preparedStatement.setLong(13, id_user);



            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                throw new SQLException("ID не найден");
            }



        });
    }

    public static void clear() {
        dbConnector.handleQuery((Connection conn) -> {
            String clearQuery = "DROP TABLE IF EXISTS cities CASCADE";
            String clearHumans = "DROP TABLE IF EXISTS humans";
            String clearCoordinates = "DROP TABLE IF EXISTS coordinates";
            Statement statement = conn.createStatement();
            statement.executeUpdate(clearQuery);
            statement.executeUpdate(clearHumans);
            statement.executeUpdate(clearCoordinates);

        });
    }

    public City getCityById(Long id) {
        return dbConnector.handleQuery((Connection conn) -> {
            String sqlQuery = "SELECT c.*, u.login as owner_username, h.name as governor_name, h.age, h.height, h.birthday " +
                    "FROM cities c " +
                    "JOIN users u ON c.owner_id = u.id " +
                    "JOIN humans h ON c.human = h.id " +
                    "WHERE c.id = ?";
            PreparedStatement statement = conn.prepareStatement(sqlQuery);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Human governor = new Human(
                        rs.getString("governor_name"),
                        rs.getInt("age"),
                        rs.getFloat("height"),
                        rs.getTimestamp("birthday").toInstant().atZone(java.time.ZoneId.systemDefault())
                );
                Coordinates coordinates = new Coordinates(
                        rs.getLong("x"),
                        rs.getLong("y")
                );
                City city = new City(
                        rs.getString("name"),
                        coordinates,
                        rs.getInt("area"),
                        rs.getLong("population"),
                        rs.getDouble("metersabovesealevel"),
                        rs.getString("capital"),
                        Climate.valueOf(rs.getString("climate")),
                        Government.valueOf(rs.getString("government")),
                        StandardOfLiving.valueOf(rs.getString("standardofliving")),
                        governor
                );
                city.setId(rs.getLong("id"));
                city.setOwnerUsername(rs.getString("owner_username"));
                city.setCreationDate(rs.getTimestamp("creation_date").toInstant().atZone(java.time.ZoneId.systemDefault()));
                return city;
            } else {
                return null;
            }
        });
    }


    public void updateCity(Long id, City city) {
        dbConnector.handleQuery((Connection conn) -> {
            conn.setAutoCommit(false);
            try {
            try {dd
                // Получаем id человека и координат для города
                String selectQuery = "SELECT human, x, y FROM cities WHERE id = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
                selectStmt.setLong(1, id);
                ResultSet rs = selectStmt.executeQuery();
                if (!rs.next()) {
                    throw new SQLException("Город с таким id не найден");
                }
                long humanId = rs.getLong("human");
                long xCoord = rs.getLong("x");
                long yCoord = rs.getLong("y");

                // Обновляем человека
                String updateHuman = "UPDATE humans SET name = ?, age = ?, height = ?, birthday = ? WHERE id = ?";
                PreparedStatement humanStmt = conn.prepareStatement(updateHuman);
                humanStmt.setString(1, city.getGovernor().getName());
                humanStmt.setInt(2, city.getGovernor().getAge());
                humanStmt.setFloat(3, city.getGovernor().getHeight());
                humanStmt.setTimestamp(4, java.sql.Timestamp.from(city.getGovernor().getBirthday().toInstant()));
                humanStmt.setLong(5, humanId);
                humanStmt.executeUpdate();

                // Обновляем координаты (если нужно)
                String updateCoordinates = "UPDATE coordinates SET x = ?, y = ? WHERE x = ? AND y = ?";
                PreparedStatement coordStmt = conn.prepareStatement(updateCoordinates);
                coordStmt.setLong(1, city.getCoordinates().getX());
                coordStmt.setLong(2, city.getCoordinates().getY());
                coordStmt.setLong(3, xCoord);
                coordStmt.setLong(4, yCoord);
                coordStmt.executeUpdate();

                // Обновляем сам город (кроме id, creation_date, owner_id)
                String updateCity = "UPDATE cities SET name = ?, area = ?, population = ?, metersabovesealevel = ?, capital = ?, climate = ?, government = ?, standardofliving = ? WHERE id = ?";
                PreparedStatement cityStmt = conn.prepareStatement(updateCity);
                cityStmt.setString(1, city.getName());
                cityStmt.setInt(2, city.getArea());
                cityStmt.setLong(3, city.getPopulation());
                cityStmt.setDouble(4, city.getMetersAboveSeaLevel());
                cityStmt.setString(5, city.getCapital());
                cityStmt.setString(6, city.getClimate().toString());
                cityStmt.setString(7, city.getGovernment().toString());
                cityStmt.setString(8, city.getStandardOfLiving().toString());
                cityStmt.setLong(9, id);
                cityStmt.executeUpdate();

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при обновлении города: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
            return null;
        });
    }

    public void clearUserObjects(String username) {
        dbConnector.handleQuery((Connection conn) -> {
            // Получаем ID пользователя
            String userIdQuery = "SELECT id FROM users WHERE login = ?";
            PreparedStatement userStmt = conn.prepareStatement(userIdQuery);
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();
            if (!userRs.next()) {
                throw new SQLException("Пользователь не найден");
            }
            long userId = userRs.getLong(1);

            // Получаем все города пользователя
            String citiesQuery = "SELECT human FROM cities WHERE owner_id = ?";
            PreparedStatement citiesStmt = conn.prepareStatement(citiesQuery);
            citiesStmt.setLong(1, userId);
            ResultSet citiesRs = citiesStmt.executeQuery();

            // Удаляем города пользователя
            String deleteCitiesQuery = "DELETE FROM cities WHERE owner_id = ?";
            PreparedStatement deleteCitiesStmt = conn.prepareStatement(deleteCitiesQuery);
            deleteCitiesStmt.setLong(1, userId);
            deleteCitiesStmt.executeUpdate();

            // Удаляем humans, которые больше не используются
            String deleteHumansQuery = "DELETE FROM humans WHERE id NOT IN (SELECT human FROM cities)";
            PreparedStatement deleteHumansStmt = conn.prepareStatement(deleteHumansQuery);
            deleteHumansStmt.executeUpdate();

            // Удаляем coordinates, которые больше не используются
            String deleteCoordinatesQuery = "DELETE FROM coordinates WHERE id_coordinate NOT IN (SELECT x FROM cities UNION SELECT y FROM cities)";
            PreparedStatement deleteCoordinatesStmt = conn.prepareStatement(deleteCoordinatesQuery);
            deleteCoordinatesStmt.executeUpdate();
        });
    }

    public boolean checkCityExisting(Long id) {
        return dbConnector.handleQuery((Connection conn) -> {
            String existingQuery = "SELECT COUNT(*) AS count FROM cities WHERE cities.id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(existingQuery);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            return resultSet.getInt("count") > 0;

        });
    }

    public void addUser(String username, String password) {
        dbConnector.handleQuery((Connection conn) -> {
            String addQuery = "INSERT INTO users (login, password) "
                  + "VALUES (?, ?);";
            PreparedStatement preparedStatement = conn.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);


            preparedStatement.executeUpdate();


        });

    }



    public String getPassword(String login) {
        return dbConnector.handleQuery((Connection connection) -> {
           String getPassQuery = "SELECT password FROM users WHERE users.login = ?";
           PreparedStatement preparedStatement = connection.prepareStatement(getPassQuery, Statement.RETURN_GENERATED_KEYS);
           preparedStatement.setString(1, login);

           ResultSet resultSet = preparedStatement.executeQuery();
           if (resultSet.next()) {
               return resultSet.getString("password");
           } else
               return null;
        });
    }

    public String getUsername(Long id) {
        return dbConnector.handleQuery((Connection conn) -> {
            String getUser = "SELECT login FROM users WHERE users.id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(getUser, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("login");
            } else return null;
        });
    }


    public Long getId(City city) {
        return dbConnector.handleQuery((Connection conn) -> {
            String selectAll = "SELECT owner_id FROM cities WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(selectAll, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, city.getId());
            ResultSet rs =  preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getLong("owner_id");
            } else {
                throw new SQLException("Город с таким id не найден");
            }

        });
    }



    public boolean checkUsersExistence(String username) throws DataBaseException {
        return dbConnector.handleQuery((Connection conn) -> {
            String existingUsers = "SELECT COUNT(*) AS count FROM users WHERE users.login = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(existingUsers);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            return resultSet.getInt("count") > 0;
        });
    }

    /**
     * Batch insert для массового добавления городов
     */
    public void addElementsBatch(List<City> cities, String username) {
        dbConnector.handleQuery((Connection conn) -> {
            conn.setAutoCommit(false);
            try {
                // Получаем id пользователя один раз
                String userIdQuery = "SELECT id FROM users WHERE login = ?";
                PreparedStatement userStmt = conn.prepareStatement(userIdQuery);
                userStmt.setString(1, username);
                ResultSet userRs = userStmt.executeQuery();
                if (!userRs.next()) {
                    throw new SQLException("Пользователь не найден");
                }
                long id_user = userRs.getLong(1);

                String insertHuman = "INSERT INTO humans(name, age, height, birthday) VALUES (?, ?, ?, ?)";
                String insertCoordinates = "INSERT INTO coordinates(x, y) VALUES (?, ?)";
                String addElementCity = "INSERT INTO cities "
                        + "(name, x, y, creation_date, area, population, metersabovesealevel, capital, climate, government, standardofliving, human, owner_id) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement humanStmt = conn.prepareStatement(insertHuman, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement coordStmt = conn.prepareStatement(insertCoordinates, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement cityStmt = conn.prepareStatement(addElementCity);

                for (int i = 0; i < cities.size(); i++) {
                    City city = cities.get(i);
                    // Вставляем human
                    humanStmt.setString(1, city.getGovernor().getName());
                    humanStmt.setLong(2, city.getGovernor().getAge());
                    humanStmt.setFloat(3, city.getGovernor().getHeight());
                    humanStmt.setDate(4, java.sql.Date.valueOf(city.getGovernor().getBirthday().toLocalDate()));
                    humanStmt.executeUpdate();
                    ResultSet resHuman = humanStmt.getGeneratedKeys();
                    if (!resHuman.next()) throw new SQLException("Не удалось получить id человека");
                    long id_human = resHuman.getLong(1);

                    // Вставляем coordinates
                    coordStmt.setLong(1, city.getCoordinates().getX());
                    coordStmt.setLong(2, city.getCoordinates().getY());
                    coordStmt.executeUpdate();
                    // id координат не используется, но можно получить если нужно

                    // Вставляем город
                    cityStmt.setString(1, city.getName());
                    cityStmt.setLong(2, city.getCoordinates().getX());
                    cityStmt.setLong(3, city.getCoordinates().getY());
                    cityStmt.setDate(4, java.sql.Date.valueOf(city.getCreationDate().toLocalDate()));
                    cityStmt.setInt(5, city.getArea());
                    cityStmt.setLong(6, city.getPopulation());
                    cityStmt.setDouble(7, city.getMetersAboveSeaLevel());
                    cityStmt.setString(8, city.getCapital());
                    cityStmt.setString(9, city.getClimate().toString());
                    cityStmt.setString(10, city.getGovernment().toString());
                    cityStmt.setString(11, city.getStandardOfLiving().toString());
                    cityStmt.setLong(12, id_human);
                    cityStmt.setLong(13, id_user);
                    cityStmt.addBatch();

                    // Прогресс-бар (каждые 10 объектов)
                    if ((i + 1) % 10 == 0 || i == cities.size() - 1) {
                        System.out.printf("Добавлено %d/%d городов...\n", i + 1, cities.size());
                    }
                }
                cityStmt.executeBatch();
                conn.commit();
                System.out.println("Массовая вставка завершена успешно!");
            } catch (Exception e) {
                conn.rollback();
                throw new RuntimeException("Ошибка при batch insert: " + e.getMessage(), e);
            } finally {
                conn.setAutoCommit(true);
            }
            return null;
        });
    }

}
