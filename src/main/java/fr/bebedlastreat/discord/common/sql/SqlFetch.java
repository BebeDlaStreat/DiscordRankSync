package fr.bebedlastreat.discord.common.sql;

import fr.bebedlastreat.discord.common.interfaces.IDatabaseFetch;

import java.sql.SQLException;
import java.util.UUID;

public class SqlFetch implements IDatabaseFetch {

    private final SqlHandler sql;

    public SqlFetch(SqlHandler sql) {
        this.sql = sql;
    }

    @Override
    public void insert(UUID uuid, String name, String discord) {
        sql.update("INSERT INTO " + sql.getTable() + " (uuid, name, discord) VALUES (?, ?, ?)", uuid.toString(), name, discord);
    }

    @Override
    public void update(UUID uuid, String name) {
        sql.update("UPDATE " + sql.getTable() + " SET name=? WHERE uuid=?", name, uuid.toString());
    }

    @Override
    public boolean firstLink(UUID uuid) {
        return (Boolean) sql.query("SELECT uuid FROM " + sql.getAllTimeTable() + " WHERE uuid='" + uuid + "'", rs -> {
            boolean result = true;
            try {
                if (rs.next()) {
                    result = false;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return result;
        });
    }

    @Override
    public void insertFirstLink(UUID uuid) {
        sql.update("INSERT INTO " + sql.getAllTimeTable() + " (uuid) VALUES (?)", uuid.toString());
    }

    @Override
    public void delete(UUID uuid) {
        sql.update("DELETE FROM " + sql.getTable() + " WHERE uuid=?", uuid.toString());
    }

    @Override
    public void delete(String discord) {
        sql.update("DELETE FROM " + sql.getTable() + " WHERE discord=?", discord);
    }

    @Override
    public boolean exist(String discord) {
        return uuid(discord).length() > 0;
    }

    @Override
    public boolean exist(UUID uuid) {
        return discord(uuid).length() > 0;
    }

    @Override
    public String discord(UUID uuid) {
        return (String) sql.query("SELECT discord FROM " + sql.getTable() + " WHERE uuid='" + uuid + "'", rs -> {
            String discord = "";
            try {
                if (rs.next()) {
                    discord = rs.getString("discord");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return discord;
        });
    }

    @Override
    public String discord(String name) {
        return (String) sql.query("SELECT discord FROM " + sql.getTable() + " WHERE name='" + name + "'", rs -> {
            String discord = "";
            try {
                if (rs.next()) {
                    discord = rs.getString("discord");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return discord;
        });
    }

    @Override
    public String uuid(String discord) {
        return (String) sql.query("SELECT uuid FROM " + sql.getTable() + " WHERE discord='" + discord + "'", rs -> {
            String uuid = "";
            try {
                if (rs.next()) {
                    uuid = rs.getString("uuid");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return uuid;
        });
    }

    @Override
    public String name(UUID uuid) {
        return (String) sql.query("SELECT name FROM " + sql.getTable() + " WHERE uuid='" + uuid + "'", rs -> {
            String discord = "";
            try {
                if (rs.next()) {
                    discord = rs.getString("name");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return discord;
        });
    }

    @Override
    public int count() {
        return (Integer) sql.query("SELECT COUNT(*) FROM " + sql.getTable(), rs -> {
            int result = 0;
            try {
                if (rs.next()) {
                    result = rs.getInt("COUNT(*)");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return result;
        });
    }

}
