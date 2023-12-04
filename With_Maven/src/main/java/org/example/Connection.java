package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;
import com.mongodb.client.*;
import com.mongodb.client.model.InsertManyOptions;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Connection {

    public static void main(String[] args) {
        String uri = "mongodb+srv://js11692:admin@cluster0.e6hjphf.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            System.out.println("=> Print list of databases:");
            /**
             * Listing Databases
             */
//            List<Document> databases = mongoClient.listDatabases().into(new ArrayList<>());
//            databases.forEach(db -> System.out.println(db.toJson()));

            /**
             * Reading from a database
             */
            MongoDatabase sampDB = mongoClient.getDatabase("sample_airbnb");
            MongoCollection<Document> listingsCollection = sampDB.getCollection("listingsAndReviews");

            // find one document with Filters.eq()
            // Assuming listing_url is a string in the MongoDB collection, pass a string to the eq filter
            Document listing = listingsCollection.find(eq("beds", "5")).first();
            if (listing != null) {
                System.out.println("Listing URL: " + listing.toJson());
            } else {
                System.out.println("No listing found with that URL");
            }

        }
    }

    /**
     * Create a Document
     */
//    public static void addNewPlaylist(){
//        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {
//
//            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
//            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");
//
////            insertOneDocument(gradesCollection);
////            insertManyDocuments(gradesCollection);
//        }
//    }

//    private static void insertOneDocument(MongoCollection<Document> gradesCollection) {
//        gradesCollection.insertOne(generateNewGrade(10000d, 1d));
//        System.out.println("One grade inserted for studentId 10000.");
//    }
//
//    private static void insertManyDocuments(MongoCollection<Document> gradesCollection) {
//        List<Document> grades = new ArrayList<>();
//        for (double classId = 1d; classId <= 10d; classId++) {
//            grades.add(newPlaylist(10001d, classId));
//        }
//
//        gradesCollection.insertMany(grades, new InsertManyOptions().ordered(false));
//        System.out.println("Ten grades inserted for studentId 10001.");
//    }
//
//    private static Document newPlaylist(double studentId, double classId) {
//        List<Document> scores = List.of(new Document("type", "exam").append("score", rand.nextDouble() * 100),
//                new Document("type", "quiz").append("score", rand.nextDouble() * 100),
//                new Document("type", "homework").append("score", rand.nextDouble() * 100),
//                new Document("type", "homework").append("score", rand.nextDouble() * 100));
//        return new Document("_id", new ObjectId()).append("student_id", studentId)
//                .append("class_id", classId)
//                .append("scores", scores);
//    }

//    static boolean preFlightChecks(MongoClient mongoClient) {
//        Document pingCommand = new Document("ping", 1);
//        Document response = mongoClient.getDatabase("admin").runCommand(pingCommand);
//        System.out.println("=> Print result of the '{ping: 1}' command.");
//        System.out.println(response.toJson(JsonWriterSettings.builder().indent(true).build()));
//
//        Object okObject = response.get("ok");
//        double okValue = okObject instanceof Integer ? ((Integer) okObject).doubleValue() : response.getDouble("ok");
//        return okValue == 1.0;
//    }


}
