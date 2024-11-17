package fr.bebedlastreat.discord.common.enums;

public enum DatabaseType {
    SQL,
    SQLITE,
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
        }
        return null;
    }
}
