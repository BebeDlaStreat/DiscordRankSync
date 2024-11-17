package fr.bebedlastreat.discord.common.sqlite;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.Getter;

import java.io.IOException;
import java.sql.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Data
public class SQLiteHandler {
    public static final String DEFAULT_DRIVER = "org.sqlite.JDBC";

    @Getter
    private static SQLiteHandler instance;
    private SQLiteCredentials credentials;
    private HikariDataSource dataSource;
    private final String table;
    private final String allTimeTable;
    private final String boostTable;

    public SQLiteHandler(SQLiteCredentials credentials, String table) {
        this.credentials = credentials;
        instance = this;
        this.table = table;
        this.allTimeTable = table + "_alltime";
        this.boostTable = table + "_boost";
    }

    private void setup() {
        //System.setProperty("com.zaxxer.hikari.housekeeping.periodMs", "1000");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(credentials.toUrl());
        config.setDriverClassName(credentials.getDriver());

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(2000);
        config.setConnectionTestQuery("SELECT 1;");
        config.setInitializationFailTimeout(0);

        dataSource = new HikariDataSource(config);
    }

    public void initPool() {
        setup();
    }

    public void close() {
        dataSource.close();
    }

    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            setup();
        }
        return dataSource.getConnection();
    }


    public void update(String qry, Object... objects){
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(qry)) {
            int i = 1;
            for (Object object : objects) {
                if (object instanceof String) {
                    s.setString(i, (String) object);
                } else if (object instanceof Long) {
                    s.setLong(i, (Long) object);
                } else if (object instanceof Integer) {
                    s.setInt(i, (Integer) object);
                } else if (object instanceof Double) {
                    s.setDouble(i, (Double) object);
                } else if (object instanceof Float) {
                    s.setFloat(i, (Float) object);
                } else if (object instanceof Byte) {
                    s.setByte(i, (Byte) object);
                } else if (object instanceof Short) {
                    s.setShort(i, (Short) object);
                } else if (object instanceof Boolean) {
                    s.setBoolean(i, (Boolean) object);
                } else {
                    s.setObject(i, object);
                }
                i++;
            }
            s.executeUpdate();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public Object query(String qry, Function<ResultSet, Object> consumer) {
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(qry);
             ResultSet rs = s.executeQuery()) {
            return consumer.apply(rs);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void query(String qry, Consumer<ResultSet> consumer) {
        try (Connection c = getConnection();
             PreparedStatement s = c.prepareStatement(qry);
             ResultSet rs = s.executeQuery()) {
            consumer.accept(rs);
        } catch (SQLException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    public void createDefault() {
        update("CREATE TABLE IF NOT EXISTS " + table + "(" +
                "uuid VARCHAR(64) PRIMARY KEY NOT NULL, " +
                "name VARCHAR(32) NOT NULL," +
                "discord VARCHAR(32) NOT NULL UNIQUE" +
                ")");
        update("CREATE TABLE IF NOT EXISTS " + allTimeTable + "(" +
                "uuid VARCHAR(64) PRIMARY KEY NOT NULL" +
                ")");
        update("CREATE TABLE IF NOT EXISTS " + boostTable + "(" +
                "uuid VARCHAR(64) PRIMARY KEY NOT NULL, " +
                "finish_time BIGINT" +
                ")");
    }
}
