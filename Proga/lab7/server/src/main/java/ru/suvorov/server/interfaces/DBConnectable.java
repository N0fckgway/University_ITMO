package ru.suvorov.server.interfaces;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import ru.suvorov.server.exception.DataBaseException;

public interface DBConnectable {
    void handleQuery(SQLConsumer<Connection> queryBody) throws SQLException, IOException;

    <T> T handleQuery(SQLFunction<Connection, T> queryBody);

}
