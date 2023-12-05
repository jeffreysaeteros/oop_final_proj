package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.util.*;


public class PlaylistManager {
    private List<Playlist> playlists;
    private static final String URI = "mongodb+srv://forGrader:grader@cluster0.e6hjphf.mongodb.net/?retryWrites=true&w=majority";
    private static final String DATABASE = "PlaylistDB";
    private static final String COLLECTION = "Playlists";


    public PlaylistManager() {
        this.playlists = new ArrayList<>();
    }


    // create new playlist
    public String createPlaylist(String name) {
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
                return name;
            }
        }
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
        return null; // no playlist found
    }

    public Playlist getPlaylist(String playlistName) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);

            // Find the document where the name matches the playlistName
            Document doc = collection.find(Filters.eq("name", playlistName)).first();
            if (doc != null) {
                // If the document is found, convert it to a Playlist object
                return documentToPlaylist(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private Playlist documentToPlaylist(Document doc) {
        Playlist playlist = new Playlist(doc.getString("name"));

        // Check if the document has a 'songs' field which is an array
        List<String> songNames = doc.getList("songs", String.class); // Directly get the list of song names
        if (songNames != null) {
            for (String songName : songNames) {
                // creates a Song object with just name
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

            // $pull operator to remove a song by its name from the "songs" array
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


    public void deletePlaylist(String playlistName) {
        try (MongoClient mongoClient = MongoClients.create(URI)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE);
            MongoCollection<Document> collection = database.getCollection(COLLECTION);
            collection.deleteOne(Filters.eq("name", playlistName));
        }
    }

}

