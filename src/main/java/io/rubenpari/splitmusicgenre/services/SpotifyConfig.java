package io.rubenpari.splitmusicgenre.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

import java.net.URI;

@Configuration
public class SpotifyConfig {
    private final String clientId;
    private final String clientSecret;
    private final URI redirectUri;

    public SpotifyConfig(EnvConfig envConfig) {
        this.clientId = envConfig.getValue("CLIENT_ID");
        this.clientSecret = envConfig.getValue("CLIENT_SECRET");
        this.redirectUri = SpotifyHttpManager.makeUri(envConfig.getValue("REDIRECT_URI"));
    }

    @Bean
    public SpotifyApi spotifyApi() {
        return new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();
    }
}
