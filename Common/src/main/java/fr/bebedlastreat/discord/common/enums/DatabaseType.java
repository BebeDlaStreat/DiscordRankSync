package fr.bebedlastreat.discord.common.enums;

public enum DatabaseType {
    SQL,
    SQLITE,
    MONGODB
    ;

    public static DatabaseType getByName(String name) {
        switch (name.toLowerCase()) {
            case "sql":
            case "mariadb":
            case "mysql": {
                return DatabaseType.SQL;
            }
            case "sqlite": {
                return DatabaseType.SQLITE;
            }
            case "mongodb": {
                return DatabaseType.MONGODB;
            }
        }
        return null;
    }
}
