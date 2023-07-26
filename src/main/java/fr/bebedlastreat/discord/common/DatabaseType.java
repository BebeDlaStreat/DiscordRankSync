package fr.bebedlastreat.discord.common;

public enum DatabaseType {
    SQL;

    public static DatabaseType getByName(String name) {
        switch (name.toLowerCase()) {
            case "sql":
            case "mariadb":
            case "mysql": {
                return DatabaseType.SQL;
            }
        }
        return null;
    }
}
