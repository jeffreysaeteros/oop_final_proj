package org.example;

import javax.swing.*;
import java.awt.*;
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
    private DefaultListModel<String> forViewPlaylist;

    private final PlaylistManager playlistManager;

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

        // mainPage: 3 buttons --> createPlaylistButton, searchPlaylistButton, viewPlaylistsButton
        JPanel mainPage = new JPanel();
        JButton createPlaylistButton = new JButton("Create Playlist");
        JButton searchPlaylistButton = new JButton("Search Playlist");
        JButton viewPlaylistsButton = new JButton("View Playlists");
        mainPage.add(createPlaylistButton);
        mainPage.add(searchPlaylistButton);
        mainPage.add(viewPlaylistsButton);

        // addSongCard: 1 Main Panel that has 3 components (since it is a BorderLayout)
            // 3 components --> headerLabel, viewSongPanel, buttonPanel
        JPanel addSongCard = new JPanel(new BorderLayout());

        // Component -->  headerLabel: At the top (BorderLayout.NORTH) of the addSongCard panel
            // In this component, contains the label which is the currentPlaylistName we are adding our deleting songs from
        headerLabel = new JLabel("", SwingConstants.CENTER);
        addSongCard.add(headerLabel, BorderLayout.NORTH);

        // Component --> viewSongsPanel: At the center (BorderLayout.CENTER) of the addSongCard panel
            // In this component, all the songs in the current playlist are shown
        listModel = new DefaultListModel<>();
        JList<String> songList = new JList<>(listModel);
        JPanel viewSongsPanel = new JPanel(new BorderLayout());
        viewSongsPanel.add(new JScrollPane(songList), BorderLayout.CENTER);
        addSongCard.add(viewSongsPanel, BorderLayout.CENTER);

        // Component --> buttonPanel: At the bottom (BorderLayout.SOUTH) of the addSongCard panel
            // In this component --> 3 buttons, addSongButton, backToMainPageButton, deleteSongButton
        JButton addSongButton = new JButton("Add Song to Playlist");
        JButton backToMainPageButton = new JButton("Back");
        JButton deleteSongButton = new JButton("Delete Song");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addSongButton);
        buttonPanel.add(backToMainPageButton);
        buttonPanel.add(deleteSongButton);
        addSongCard.add(buttonPanel, BorderLayout.SOUTH);

        // searchPanel: 2 components --> searchField, searchButton
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // viewPlaylistsPanel: to view all playlists in our database
        JPanel viewPlaylistsPanel = new JPanel(new BorderLayout());
        JLabel viewPlaylistLabel = new JLabel("All Playlists", SwingConstants.CENTER);
        JPanel viewPlaylistButtonsPanel = new JPanel();
        forViewPlaylist = new DefaultListModel<>();
        JList<String> playlistList = new JList<>(forViewPlaylist);
        JButton deletePlaylistButton = new JButton("Delete Playlist");
        JButton backToMainFromViewPlaylist = new JButton("Back");
        viewPlaylistsPanel.add(new JScrollPane(playlistList), BorderLayout.CENTER);
        viewPlaylistsPanel.add(viewPlaylistLabel, BorderLayout.NORTH);
        viewPlaylistButtonsPanel.add(deletePlaylistButton);
        viewPlaylistButtonsPanel.add(backToMainFromViewPlaylist);
        viewPlaylistsPanel.add(viewPlaylistButtonsPanel, BorderLayout.SOUTH);


        // Here are the panels that we have available to us, and we add each panel to our CardLayout cards
        cards.add(mainPage, MAIN_PAGE_PANEL);
        cards.add(addSongCard, ADD_SONG_PANEL);
        cards.add(searchPanel, SEARCH_PANEL);
        cards.add(viewPlaylistsPanel, VIEW_PANEL);


        // button action listeners
        createPlaylistButton.addActionListener(e -> {
            createPlaylist();
        });
        backToMainPageButton.addActionListener(e->{
            cardLayout.show(cards, MAIN_PAGE_PANEL);
        });
        backToMainFromViewPlaylist.addActionListener(e->{
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
                    // playlist found
                    currPlaylistName = foundPlaylist.getName(); // Set the current playlist name to the found playlist
                    headerLabel.setText(currPlaylistName); // Update the header with playlist name
                    displaySongsInPlaylist(currPlaylistName); // Display the songs in the playlist
                    cardLayout.show(cards, ADD_SONG_PANEL);
                } else {
                    // Playlist not found - show message and clear old data
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
        deletePlaylistButton.addActionListener(e -> {
            int selectedIndex = playlistList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedPlaylist = forViewPlaylist.getElementAt(selectedIndex);
                int confirm = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to delete the playlist '" + selectedPlaylist + "'?",
                        "Delete Playlist", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    playlistManager.deletePlaylist(selectedPlaylist);
                    viewPlaylists(); // Refresh the playlist view
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a playlist to delete.");
            }
        });

        // Add the cards to the frame
        frame.add(cards);
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null); // Placing frame in center of screen
        frame.setVisible(true);
    }


    // Method to create a playlist
    private void createPlaylist() {
        String name = JOptionPane.showInputDialog(frame, "Enter Playlist Name:");
        if (name != null && !name.trim().isEmpty()) {
            // Search first to prevent duplicates -> only create a new playlist if it doesn't exist
            Playlist existingPlaylist = playlistManager.searchPlaylist(name);
            if (existingPlaylist == null) {
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

            // get the existing playlist and add the song to it
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
        listModel.clear();
        for (Song song : songs) {
            listModel.addElement(song.getName());
        }
    }

    private void viewPlaylists() {
        List<Playlist> playlists = playlistManager.getAllPlaylists();
        forViewPlaylist.clear();
        for(Playlist playlist:playlists){
            forViewPlaylist.addElement(playlist.getName());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}
