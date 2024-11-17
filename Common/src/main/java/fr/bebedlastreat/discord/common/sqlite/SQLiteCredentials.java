package fr.bebedlastreat.discord.common.sqlite;

import lombok.Data;

import java.io.File;

@Data
public class SQLiteCredentials {
    private final File file;
    private final String driver;


    public SQLiteCredentials(File file, String driver) {
        this.file = file;
        this.driver = driver;
    }

    public String toUrl() {
        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:sqlite:")
                .append(file);
        return builder.toString();
    }
}
