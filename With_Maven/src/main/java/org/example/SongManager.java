//package org.example;
//
//public class SongManager {
//
//    private PlaylistManager playlistManager;
//
//    public SongManager(PlaylistManager playlistManager) {
//        this.playlistManager = playlistManager;
//    }
//
//    // adding song to playlist
//    public void addSongToPlaylist(String playlistName, Song song) {
//        Playlist playlist = playlistManager.getPlaylist(playlistName);
//        if (playlist != null) {
//            playlist.addSong(song);
//        } else {
//            System.out.println("Playlist not found: " + playlistName);
//        }
//    }
//
//    // removing a song from a playlist
//    public void removeSongFromPlaylist(String playlistName, Song song) {
//        Playlist playlist = playlistManager.getPlaylist(playlistName);
//        if (playlist != null) {
//            playlist.removeSong(song);
//        } else {
//            System.out.println("Playlist not found: " + playlistName);
//        }
//    }
//
//    // search song in a playlist
//    public Song searchSongInPlaylist(String playlistName, String songName) {
//        Playlist playlist = playlistManager.getPlaylist(playlistName);
//        if (playlist != null) {
//            for (Song song : playlist.getSongs()) {
//                if (song.getName().equals(songName)) {
//                    return song;
//                }
//            }
//        } else {
//            System.out.println("Playlist not found: " + playlistName);
//        }
//        return null; // song not found
//    }
//}