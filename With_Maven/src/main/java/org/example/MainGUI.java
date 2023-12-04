package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainGUI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards; // a panel that uses CardLayout
    private static final String ADD_SONG_PANEL = "Add Song to Playlist Panel";
    private static final String MAIN_PAGE_PANEL = "Main Page Panel";
    private static final String SEARCH_PANEL = "Search Playlist";
    private static final String VIEW_PANEL = "View Playlists Panel";

    private String currPlaylistName;
    private JLabel headerLabel;
    private DefaultListModel<String> listModel;
    private PlaylistManager playlistManager;

//    private JPanel viewPlaylistsPanel;
    private DefaultListModel<String> playlistListModel;

    // Constructor
    public MainGUI() {
        playlistManager = new PlaylistManager();
        initializeGUI();
    }
    private void initializeGUI() {
        frame = new JFrame("Playlist Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        JPanel addSongCard = new JPanel(new BorderLayout());
        headerLabel = new JLabel("", SwingConstants.CENTER);
        JButton addSongButton = new JButton("Add Song to Playlist");
        JButton goBackToMainPageButton = new JButton("Back");

        // Create a panel for the buttons and add the buttons to it
        JButton deleteSongButton = new JButton("Delete Song");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addSongButton);
        buttonPanel.add(goBackToMainPageButton);
        buttonPanel.add(deleteSongButton);

        // Add the header label to the NORTH region
        addSongCard.add(headerLabel, BorderLayout.NORTH);

        // Add the button panel to the SOUTH region
        addSongCard.add(buttonPanel, BorderLayout.SOUTH);

        listModel = new DefaultListModel<>();
        JList<String> songList = new JList<>(listModel);

        // Add the song list to the CENTER region
        JPanel viewSongsPanel = new JPanel(new BorderLayout());
        viewSongsPanel.add(new JScrollPane(songList), BorderLayout.CENTER);
        addSongCard.add(viewSongsPanel, BorderLayout.CENTER); // Ensure this is in the CENTER

        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel viewPlaylistsPanel = new JPanel(new BorderLayout());
        playlistListModel = new DefaultListModel<>();
        JList<String> playlistsList = new JList<>(playlistListModel);
        viewPlaylistsPanel.add(new JScrollPane(playlistsList), BorderLayout.CENTER);




        JPanel mainPage = new JPanel();
        JButton createPlaylistButton = new JButton("Create Playlist");
        JButton searchPlaylistButton = new JButton("Search Playlist");
        JButton viewPlaylistsButton = new JButton("View Playlists");
        mainPage.add(createPlaylistButton);
        mainPage.add(searchPlaylistButton);
        mainPage.add(viewPlaylistsButton);

        cards.add(mainPage, MAIN_PAGE_PANEL);
        cards.add(addSongCard, ADD_SONG_PANEL); //viewCurrentPlaylist
        cards.add(searchPanel, SEARCH_PANEL);
        cards.add(viewPlaylistsPanel, VIEW_PANEL);


        /* CREATE PLAYLIST --> ADD SONGS TO PLAYLIST || BACK
        click createPlaylistButton ---> addSongButtons
        */
        createPlaylistButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter Playlist Name:");
            if (name != null && !name.trim().isEmpty()) {
                // Search first to prevent duplicates
//                Playlist existingPlaylist = playlistManager.searchPlaylist(name);
                Playlist existingPlaylist = null;
                if (existingPlaylist == null) {
                    // Only create a new playlist if it doesn't exist
                    currPlaylistName = playlistManager.createPlaylist(name);
                    JOptionPane.showMessageDialog(frame, "Playlist created successfully!");
                    headerLabel.setText(currPlaylistName);
                    displaySongsInPlaylist(currPlaylistName);
                    cardLayout.show(cards, ADD_SONG_PANEL);
                } else {
                    JOptionPane.showMessageDialog(frame, "Playlist already exists.");
                }
            }
        });
        goBackToMainPageButton.addActionListener(e->{
            cardLayout.show(cards, MAIN_PAGE_PANEL);
        });
        addSongButton.addActionListener(e->{
            addSongToPlaylist();
        });

        searchPlaylistButton.addActionListener(e -> {
            cardLayout.show(cards, SEARCH_PANEL);
        });
        searchButton.addActionListener(e -> {
            String playlistName = searchField.getText().trim();
            if (!playlistName.isEmpty()) {
                Playlist foundPlaylist = playlistManager.searchPlaylist(playlistName);
                if (foundPlaylist != null) {
                    // Playlist found - update the GUI to display the playlist's details
                    currPlaylistName = foundPlaylist.getName(); // Set the current playlist name to the found playlist
                    headerLabel.setText(currPlaylistName); // Update the header with the playlist's name
                    displaySongsInPlaylist(currPlaylistName); // Display the songs in the playlist
                    cardLayout.show(cards, ADD_SONG_PANEL); // Switch to the panel that displays the songs
                } else {
                    // Playlist not found - show a message and clear any old data
                    JOptionPane.showMessageDialog(frame, "Playlist not found.");
                    headerLabel.setText("");
                    listModel.clear();
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please enter a playlist name to search.");
            }
        });
        deleteSongButton.addActionListener(e -> {
            int selectedIndex = songList.getSelectedIndex();
            if (selectedIndex != -1) {
                int confirm = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to delete this song?",
                        "Delete Song",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    String songName = listModel.get(selectedIndex);
                    playlistManager.deleteSongFromPlaylist(headerLabel.getText(), songName);
                    displaySongsInPlaylist(headerLabel.getText()); // Refresh the song list display
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a song to delete.");
            }
        });
        viewPlaylistsButton.addActionListener(e -> {
            viewPlaylists();
            cardLayout.show(cards, VIEW_PANEL);
        });

        // Add the cards to the frame
        frame.add(cards);
        // Set frame properties
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    // Method to create a playlist
    private void createPlaylist() {
        String name = JOptionPane.showInputDialog(frame, "Enter Playlist Name:");
        if (name != null && !name.trim().isEmpty()) {
            // Search first to prevent duplicates
            Playlist existingPlaylist = playlistManager.searchPlaylist(name);
            if (existingPlaylist == null) {
                // Only create a new playlist if it doesn't exist
                currPlaylistName = playlistManager.createPlaylist(name);
                JOptionPane.showMessageDialog(frame, "Playlist created successfully!");
                headerLabel.setText(currPlaylistName);
                displaySongsInPlaylist(currPlaylistName);
                cardLayout.show(cards, ADD_SONG_PANEL);
            } else {
                JOptionPane.showMessageDialog(frame, "Playlist already exists.");
            }
        }
    }

    private void addSongToPlaylist() {
        String songName = JOptionPane.showInputDialog(frame, "Enter Song Name:");
        if (songName != null && !songName.trim().isEmpty()) {
            Song song = new Song(songName);

            // Get the existing playlist and add the song to it
            Playlist playlist = playlistManager.getPlaylist(currPlaylistName);
            if (playlist != null) {
                playlist.addSong(song);
                JOptionPane.showMessageDialog(frame, "Song added to playlist!");
                displaySongsInPlaylist(currPlaylistName); // Refresh the song list display
            } else {
                JOptionPane.showMessageDialog(frame, "Playlist not found.");
            }
        }
    }


    private void displaySongsInPlaylist(String playlistName) {
        List<Song> songs = playlistManager.getSongsFromPlaylist(playlistName);
        listModel.clear(); // Clear existing content
        for (Song song : songs) {
            listModel.addElement(song.getName()); // Assuming Song has a getName method
        }
    }

    private void viewPlaylists() {
        List<Playlist> playlists = playlistManager.getAllPlaylists();
        StringBuilder playlistsInfo = new StringBuilder();
        for (Playlist p : playlists) {
            playlistsInfo.append(p.getName()).append(": ");
            for (Song s : p.getSongs()) {
                playlistsInfo.append(s.getName()).append(", ");
            }
            playlistsInfo.append("\n");
        }
        JOptionPane.showMessageDialog(frame, playlistsInfo.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI());
    }
}
