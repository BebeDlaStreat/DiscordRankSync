package fr.bebedlastreat.discord.common.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.Getter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

@Data
public class SqlHandler {
    @Getter
    private static SqlHandler instance;
    private SqlCredentials credentials;
    private HikariDataSource dataSource;
    private int maxPoolSize;
    private int minIdle;
    private long maxLifetime;
    private long keepaliveTime;
    private long connectionTimeout;
    private String table;

    public SqlHandler(SqlCredentials credentials, int maxPoolSize, int minIdle, long maxLifetime, long keepaliveTime, long connectionTimeout, String table) {
        this.credentials = credentials;
        instance = this;
        this.maxPoolSize = maxPoolSize;
        this.minIdle = minIdle;
        this.maxLifetime = maxLifetime;
        this.keepaliveTime = keepaliveTime;
        this.connectionTimeout = connectionTimeout;
        this.table = table;
    }

    private void setup() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setMaxLifetime(maxLifetime);
        config.setKeepaliveTime(keepaliveTime);
        config.setConnectionTimeout(connectionTimeout);
        config.setInitializationFailTimeout(-1);
        config.setJdbcUrl(credentials.toUrl());
        config.setUsername(credentials.getUser());
        config.setPassword(credentials.getPass());

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
    }
}
