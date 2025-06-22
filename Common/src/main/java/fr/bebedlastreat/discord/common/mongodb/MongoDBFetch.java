package fr.bebedlastreat.discord.common.mongodb;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import fr.bebedlastreat.discord.common.interfaces.IDatabaseFetch;
import org.bson.Document;

import java.util.UUID;

public class MongoDBFetch implements IDatabaseFetch {

    private final MongoDBHandler mongodb;

    public MongoDBFetch(MongoDBHandler mongodb) {
        this.mongodb = mongodb;
    }

    @Override
    public void insert(UUID uuid, String name, String discord) {
        Document doc = new Document("uuid", uuid.toString())
                .append("name", name)
                .append("discord", discord);
        mongodb.getBaseCollection().insertOne(doc);
    }

    @Override
    public void update(UUID uuid, String name) {
        mongodb.getBaseCollection().updateOne(
                Filters.eq("uuid", uuid.toString()),
                Updates.set("name", name)
        );
    }

    @Override
    public boolean firstLink(UUID uuid) {
        return mongodb.getAllTimeCollection().find(Filters.eq("uuid", uuid.toString())).first() == null;
    }

    @Override
    public void insertFirstLink(UUID uuid) {
        Document doc = new Document("uuid", uuid.toString());
        mongodb.getAllTimeCollection().insertOne(doc);
    }

    @Override
    public void delete(UUID uuid) {
        mongodb.getBaseCollection().deleteOne(Filters.eq("uuid", uuid.toString()));
    }

    @Override
    public void delete(String discord) {
        mongodb.getBaseCollection().deleteOne(Filters.eq("discord", discord));
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
        Document doc = mongodb.getBaseCollection().find(Filters.eq("uuid", uuid.toString())).first();
        return doc != null ? doc.getString("discord") : "";
    }

    @Override
    public String discord(String name) {
        Document doc = mongodb.getBaseCollection().find(Filters.eq("name", name)).first();
        return doc != null ? doc.getString("discord") : "";
    }

    @Override
    public String uuid(String discord) {
        Document doc = mongodb.getBaseCollection().find(Filters.eq("discord", discord)).first();
        return doc != null ? doc.getString("uuid") : "";
    }

    @Override
    public String name(UUID uuid) {
        Document doc = mongodb.getBaseCollection().find(Filters.eq("uuid", uuid.toString())).first();
        return doc != null ? doc.getString("name") : "";
    }

    @Override
    public int count() {
        return (int) mongodb.getBaseCollection().countDocuments();
    }

    @Override
    public int allTimeCount() {
        return (int) mongodb.getAllTimeCollection().countDocuments();
    }

    @Override
    public void insertBoost(UUID uuid, long time) {
        Document doc = new Document("uuid", uuid.toString())
                .append("finish_time", time);
        mongodb.getBoostCollection().insertOne(doc);
    }

    @Override
    public void deleteBoost(UUID uuid) {
        mongodb.getBoostCollection().deleteOne(Filters.eq("uuid", uuid.toString()));
    }

    @Override
    public boolean canBoost(UUID uuid) {
        Document doc = mongodb.getBoostCollection().find(Filters.eq("uuid", uuid.toString())).first();
        if (doc == null) return true;
        long time = doc.getLong("finish_time");
        return System.currentTimeMillis() > time;
    }

    @Override
    public long getNextBoost(UUID uuid) {
        Document doc = mongodb.getBoostCollection().find(Filters.eq("uuid", uuid.toString())).first();
        return doc != null ? doc.getLong("finish_time") : 0;
    }
}
