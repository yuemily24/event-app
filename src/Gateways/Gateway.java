package Gateways;

import UseCases.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import org.bson.Document;

import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import Presenters.Presenter;


public class Gateway {

    private MongoCollection<Document> users;
    private MongoCollection<Document> messages;
    private MongoCollection<Document> events;
    private MongoCollection<Document> rooms;
    private final Presenter presenter = new Presenter();

    public Gateway() {
        try {
            Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);  //get rid of this line to see MongoDB logs
            final String uriString = "mongodb+srv://user1:csc207@cluster0.qttdw.mongodb.net/projectDB?retryWrites=true&w=majority";
            // username is 'user1' password is 'csc207' dbname is 'projectDB'
            MongoClient mongoClient = MongoClients.create(uriString);
            MongoDatabase database = mongoClient.getDatabase("projectDB");
            this.users = database.getCollection("users");
            this.messages = database.getCollection("messages");
            this.events = database.getCollection("events");
            this.rooms = database.getCollection("rooms");
            presenter.print("Database connected successfully.");
        } catch (Exception e) {
            presenter.print("Database failed to connect.");
        }
    }

    /**
     * Imports a manager of a specific type from the database.
     *
     * @param managerType the type of the manager to be imported.
     * @return the Manager of the type specified.
     */
    public Manager importManager(String managerType) {
        MongoCollection<Document> collection;
        Manager manager;
        switch (managerType) {
            case "Users":
                collection = users;
                manager = new UserManager();
                break;
            case "Events":
                collection = events;
                manager = new EventManager();
                break;
            case "Messages":
                collection = messages;
                manager = new MessageManager();
                break;
            default:
                collection = rooms;
                manager = new RoomManager();
                break;
        }
        // iterate through collection
        FindIterable<Document> iterable = collection.find();
        try (MongoCursor<Document> cursor = iterable.iterator()) {
            while (cursor.hasNext()) {
                Document entity = cursor.next(); // points to a document that represents an entity
                manager.importEntity(entity);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return manager;
    }

    /**
     * Export a Manager to the database.
     *
     * @param manager The Manager to be exported.
     */
    public void exportManager(Manager manager) {
        MongoCollection<Document> collection;
        String managerType = manager.toString().contains("User") ? "Users" : manager.toString().contains("Event")
                ? "Events" : manager.toString().contains("Message") ? "Messages" : "Rooms";
        switch (managerType) {
            case "Users":
                collection = users;
                break;
            case "Events":
                collection = events;
                break;
            case "Messages":
                collection = messages;
                break;
            default:
                collection = rooms;
                break;
        }
        collection.deleteMany(new Document()); // remove all existing documents from collection
        List<Map<String, List<String>>> currentEntities = manager.getInfoAsList();
        for (Map<String, List<String>> entity : currentEntities) {
            Document newEntity = new Document();
            for (String field : entity.keySet()) { // set of keys in hashmap corresponding to entity field
                newEntity.append(field, entity.get(field));
            }
            collection.insertOne(newEntity);
        }
        presenter.print(managerType + " have successfully been exported to the database.");
    }

}
