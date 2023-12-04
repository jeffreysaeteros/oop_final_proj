package org.example;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class PlaylistManager {
    private List<Playlist> playlists;
    private static final String URI = "mongodb+srv://js11692:admin@cluster0.e6hjphf.mongodb.net/?retryWrites=true&w=majority";
    private static final String DATABASE = "PlaylistDB";
    private static final String COLLECTION = "Playlists";


    public PlaylistManager() {
        this.playlists = new ArrayList<>();
    }

    public void saveSongToDB(String playlistName, Song song) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase("PlaylistDB");
            MongoCollection<Document> collection = database.getCollection("Playlists");

            // If the playlist exists, add the song name as a string to the songs array
            Bson updateOperation = Updates.push("songs", song.getName());
            collection.updateOne(Filters.eq("name", playlistName), updateOperation);
        }
    }


    // create new playlist
    public String createPlaylist(String name) {
        System.out.println("Attempting to create a playlist with name: " + name);
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            // Check if a playlist with the same name already exists
            long count = collection.countDocuments(Filters.eq("name", name));
            if (count == 0) {
                // No playlist exists with this name, create a new one
                Document playlistDoc = new Document("name", name);
                collection.insertOne(playlistDoc);
                return name;
            } else {
                // A playlist with this name already exists, return its name or handle accordingly
                return name;
            }
        }
    }

    // add song to playlist
    public void addSongToPlaylist(String playlistName, Song song) {
        saveSongToDB(playlistName, song);
    }

    public List<Song> getSongsFromPlaylist(String playlistName) {
        List<Song> songs = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            Document playlistDoc = collection.find(Filters.eq("name", playlistName)).first();
            if (playlistDoc != null && playlistDoc.containsKey("songs")) {
                List<String> songNames = (List<String>) playlistDoc.get("songs");
                for (String songName : songNames) {
                    Song song = new Song(songName);
                    songs.add(song);
                }
            }
        }
        return songs;
    }




    public Playlist searchPlaylist(String playlistName) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            Document doc = collection.find(Filters.eq("name", playlistName)).first();
            if (doc != null) {
                return documentToPlaylist(doc);
            }
        }
        return null; // No playlist found
    }

    public Playlist getPlaylist(String playlistName) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            // Find the document where the name matches the playlistName
            Document doc = collection.find(eq("name", playlistName)).first();
            if (doc != null) {
                // If the document is found, convert it to a Playlist object
                return documentToPlaylist(doc);
            }
        } catch (Exception e) {
            // Handle exceptions such as MongoDB not available
            e.printStackTrace();
        }
        // Return null if the playlist is not found or an exception occurs
        return null;
    }


    private Playlist documentToPlaylist(Document doc) {
        Playlist playlist = new Playlist(doc.getString("name"));

        // Check if the document has a 'songs' field which is an array
        List<String> songNames = doc.getList("songs", String.class); // Directly get the list of song names
        if (songNames != null) {
            for (String songName : songNames) {
                // Create a Song object with just the name
                Song song = new Song(songName);
                playlist.addSong(song);
            }
        }
        return playlist;
    }


    public void deleteSongFromPlaylist(String playlistName, String songName) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            // Use the $pull operator to remove a song by its name from the "songs" array
            Bson updateOperation = Updates.pull("songs", songName);
            collection.updateOne(Filters.eq("name", playlistName), updateOperation);
        }
    }


    public List<Playlist> getAllPlaylists() {
        List<Playlist> allPlaylists = new ArrayList<>();
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            // Retrieve all playlist documents
            FindIterable<Document> documents = collection.find();
            for (Document doc : documents) {
                Playlist playlist = documentToPlaylist(doc);
                allPlaylists.add(playlist);
            }
        }
        return allPlaylists;
    }

}

