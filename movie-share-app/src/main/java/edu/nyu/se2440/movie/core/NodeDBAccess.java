package edu.nyu.se2440.movie.core;

import java.sql.Connection;

public class NodeDBAccess {
    private boolean readAccess;
    private boolean updateAccess;
    private Connection connection;

    public NodeDBAccess(boolean readAccess, boolean updateAccess, Connection connection) {
        this.readAccess = readAccess;
        this.updateAccess = updateAccess;
        this.connection = connection;
    }

    public boolean isReadAccess() {
        return readAccess;
    }

    public void setReadAccess(boolean readAccess) {
        this.readAccess = readAccess;
    }

    public boolean isUpdateAccess() {
        return updateAccess;
    }

    public void setUpdateAccess(boolean updateAccess) {
        this.updateAccess = updateAccess;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
