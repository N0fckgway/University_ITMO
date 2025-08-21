package ru.suvorov.server.interfaces;


import ru.suvorov.server.exception.DataBaseException;

import java.sql.SQLException;

@FunctionalInterface

public interface SQLFunction<T, R> {
    R apply(T t) throws SQLException, DataBaseException;
}
