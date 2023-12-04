package org.example;

import java.util.ArrayList;
import java.util.List;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.model.Updates;


public class Playlist {
    private String name;
    private List<Song> songs;
    private String uri = "mongodb+srv://js11692:admin@cluster0.e6hjphf.mongodb.net/?retryWrites=true&w=majority";

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();

    }

    public String getName() {
        return name;
    }

    public void addSong(Song song) {
        songs.add(song);
        updateDB();
    }

    public void updateDB() {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("PlaylistDB");
            MongoCollection<Document> collection = database.getCollection("Playlists");

            // Prepare a list of song names
            List<String> songNames = new ArrayList<>();
            for (Song song : this.songs) {
                songNames.add(song.getName());
            }

            // Update the existing playlist document with the list of song names
            Bson filter = eq("name", this.name);
            Bson updateOperation = Updates.set("songs", songNames);
            collection.updateOne(filter, updateOperation);
        }
    }


    public List<Song> getSongs() {
        return new ArrayList<>(songs);
    }
}
