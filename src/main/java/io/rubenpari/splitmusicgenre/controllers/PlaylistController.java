package io.rubenpari.splitmusicgenre.controllers;

import io.rubenpari.splitmusicgenre.services.PlaylistService;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.SavedTrack;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    /**
     * Create or recreate a playlist
     * with all the songs from the user
     * that are in the EDM genre
     */
    @PostMapping("/create-edm")
    public String createEdmPlaylist() {
        try {
            List<SavedTrack> tracks = playlistService.getAllUserTracks();

            System.out.println("Creating playlist with " + tracks.size() + " tracks");

            return "Playlist created";
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            return "Error creating playlist: " + e.getMessage();
        }
    }

}
