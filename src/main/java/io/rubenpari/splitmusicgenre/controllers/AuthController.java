package io.rubenpari.splitmusicgenre.controllers;

import io.rubenpari.splitmusicgenre.services.EnvConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final SpotifyApi spotifyApi;
    private final EnvConfig envConfig;

    public AuthController(SpotifyApi spotifyApi, EnvConfig envConfig) {
        this.spotifyApi = spotifyApi;
        this.envConfig = envConfig;
    }

    @GetMapping("/login")
    public RedirectView login() {
        String state = UUID.randomUUID().toString();

        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state(state)
                .scope(envConfig.getValue("SCOPES"))
                .show_dialog(true)
                .build();

        return new RedirectView(authorizationCodeUriRequest.execute().toString());
    }

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code) {
        try {
            AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            try {
                String userName = spotifyApi.getCurrentUsersProfile().build().execute().getDisplayName();
                System.out.println("User's name: " + userName);
            } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
                return "Error retrieving User's information: " + e.getMessage();
            }

            return "Successfully authenticated!";
        } catch (IOException | SpotifyWebApiException | org.apache.hc.core5.http.ParseException e) {
            return "Error obtaining Access Token: " + e.getMessage();
        }
    }

    @GetMapping("/logout")
    public String logout() {
        spotifyApi.setAccessToken(null);
        spotifyApi.setRefreshToken(null);
        return "Logged out successfully";
    }
}
