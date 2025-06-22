package fr.bebedlastreat.discord.common.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.Data;
import org.bson.Document;

@Data
public class MongoDBHandler {

    private final String url, db, collection;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> baseCollection;
    private MongoCollection<Document> allTimeCollection;
    private MongoCollection<Document> boostCollection;



    public MongoDBHandler(String url, String database, String collection) {
        this.url = url;
        this.db = database;
        this.collection = collection;
    }

    public void initConnection(){
        client = MongoClients.create(url);
        database = client.getDatabase(db);
        baseCollection = database.getCollection(collection);
        allTimeCollection = database.getCollection(collection + "_alltime");
        boostCollection = database.getCollection(collection + "_boost");

        baseCollection.createIndex(Indexes.ascending("uuid"), new IndexOptions().unique(true));
        allTimeCollection.createIndex(Indexes.ascending("uuid"), new IndexOptions().unique(true));
        boostCollection.createIndex(Indexes.ascending("uuid"), new IndexOptions().unique(true));

        baseCollection.createIndex(Indexes.ascending("discord"), new IndexOptions().unique(true));

    }


    public void closeConnection(){
        client.close();
    }
}
