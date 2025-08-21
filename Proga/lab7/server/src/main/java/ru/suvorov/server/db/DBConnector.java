package ru.suvorov.server.db;
import org.postgresql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.suvorov.server.exception.DataBaseException;
import ru.suvorov.server.interfaces.DBConnectable;
import ru.suvorov.server.interfaces.SQLConsumer;
import ru.suvorov.server.interfaces.SQLFunction;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;


public class DBConnector implements DBConnectable   {
    private static final Logger logger = LoggerFactory.getLogger(DBConnector.class);

    public DBConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = getConnection()) {
                logger.info("Connection to DB succesfull!");
            }
        }
        catch (Exception ex) {
            logger.info("Connection failed...");
            logger.info(String.valueOf(ex));

        }

    }

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        try (InputStream inputStream = Files.newInputStream(Paths.get("server/build/libs/properties/database.properties"))){
            props.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String name = props.getProperty("name");
        String passwd = props.getProperty("passwd");
        String dbUrl = props.getProperty("dbUrl");

        return DriverManager.getConnection(dbUrl, name, passwd);

    }

    @Override
    public void handleQuery(SQLConsumer<Connection> queryBody) {
        try (Connection connection = getConnection()) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка при работе с БД " + e.getMessage());
        }
    }

    @Override
    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) {
        try (Connection connection = getConnection()){
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DataBaseException("Ошибка при работе с БД: " + e.getMessage());
        }

    }

    public static void initDB() throws SQLException {
        Connection connection = getConnection();

        Statement statement = connection.createStatement();

        statement.execute("CREATE SEQUENCE IF NOT EXISTS city_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ");

        statement.execute("CREATE SEQUENCE IF NOT EXISTS user_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE SEQUENCE IF NOT EXISTS human_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE SEQUENCE IF NOT EXISTS coordinate_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE TABLE IF NOT EXISTS users" +
                "("
                + "login VARCHAR(128) NOT NULL UNIQUE CHECK(login<>'') , "
                + "password VARCHAR(255) NOT NULL CHECK(password<>''), "
                + "id BIGINT NOT NULL PRIMARY KEY DEFAULT NEXTVAL('user_id_seq')"
                + ")"
        );

        statement.execute("CREATE TABLE IF NOT EXISTS humans" +
                "("
                + "id BIGINT NOT NULL PRIMARY KEY DEFAULT NEXTVAL('human_id_seq'), "
                + "name VARCHAR(128) NOT NULL CHECK(name<>''), "
                + "age int CHECK(age > 0), "
                + "height float CHECK(height > 0), "
                + "birthday timestamp"
                + ")"
        );

        statement.execute("CREATE TABLE IF NOT EXISTS coordinates" +
                "("
                + "id_coordinate BIGINT NOT NULL PRIMARY KEY DEFAULT NEXTVAL('coordinate_id_seq'), "
                + "x BIGINT, "
                + "y BIGINT NOT NULL CHECK(y >= -583)"
                + ")"
        );

        statement.execute("DROP TABLE IF EXISTS cities CASCADE");

        statement.execute("CREATE TABLE cities" +
                "("
                + "id BIGINT NOT NULL PRIMARY KEY DEFAULT NEXTVAL('city_id_seq'), "
                + "name VARCHAR(128) NOT NULL CHECK(name<>''), "
                + "x BIGINT, "
                + "y BIGINT NOT NULL CHECK(y >= -583), "
                + "creation_date timestamp NOT NULL, "
                + "area int CHECK(area > 0), "
                + "population BIGINT CHECK(population > 0), "
                + "metersaboveseaLevel DOUBLE PRECISION, "
                + "capital VARCHAR(128), "
                + "climate VARCHAR(32) NOT NULL CHECK(climate = 'RAIN_FOREST' OR climate = 'MONSOON' OR climate = 'HUMIDSUBTROPICAL'), "
                + "government VARCHAR(32) NOT NULL CHECK(government = 'KLEPTOCRACY' OR government = 'PLUTOCRACY' OR government = 'REPUBLIC' OR government = 'TIMOCRACY'), "
                + "standardofliving VARCHAR(32) NOT NULL CHECK(standardOfLiving = 'HIGH' OR standardOfLiving = 'LOW' OR standardOfLiving = 'VERY_LOW' OR standardOfLiving = 'ULTRA_LOW'), "
                + "human BIGINT NOT NULL REFERENCES Humans(id), "
                + "owner_id bigint NOT NULL REFERENCES Users (id)"
                + ")"
        );

        connection.close();


    }


}
