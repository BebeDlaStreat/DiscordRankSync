package fr.bebedlastreat.discord.common.sql;

import lombok.Data;

@Data
public class SqlCredentials {
    private final String host;
    private final String user;
    private final String pass;
    private final String dbName;
    private final int port;
    private final String properties;
    private final String driver;


    public SqlCredentials(String host, String user, String pass, String dbName, int port, String properties, String driver) {
        this.host = host;
        this.user = user;
        this.pass = pass;
        this.dbName = dbName;
        this.port = port;
        this.properties = properties;
        this.driver = driver;
    }

    public String toUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:mysql://")
                .append(host).append(":").append(port)
                .append("/").append(dbName).append(properties);
        return builder.toString();
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
