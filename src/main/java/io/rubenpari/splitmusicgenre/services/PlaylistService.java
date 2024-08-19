package io.rubenpari.splitmusicgenre.services;

import org.apache.hc.core5.http.ParseException;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.SavedTrack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class PlaylistService {
    private final SpotifyConfig spotifyConfig;

    public PlaylistService(SpotifyConfig spotifyConfig) {
        this.spotifyConfig = spotifyConfig;
    }

    public List<SavedTrack> getAllUserTracks() throws IOException, SpotifyWebApiException, ParseException {
        List<SavedTrack> allTracks = new ArrayList<>();

        Paging<SavedTrack> firstPage = spotifyConfig.spotifyApi().getUsersSavedTracks().build().execute();

        Collections.addAll(allTracks, firstPage.getItems());

        while (firstPage.getNext() != null) {
            firstPage = spotifyConfig.spotifyApi()
                    .getUsersSavedTracks()
                    .offset(
                            firstPage.getOffset() + firstPage.getLimit()
                    )
                    .build()
                    .execute();
            Collections.addAll(allTracks, firstPage.getItems());
        }

        return allTracks;
    }
}
