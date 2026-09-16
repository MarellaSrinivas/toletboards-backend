package com.toletboards.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleTokenService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenService(
            @Value("${google.client-id}") String clientId) {

        this.verifier =
                new GoogleIdTokenVerifier.Builder(
                        new NetHttpTransport(),
                        GsonFactory.getDefaultInstance()
                )
                .setAudience(
                        Collections.singletonList(clientId)
                )
                .build();
    }

    public GoogleIdToken.Payload verify(String credential)
        throws Exception {

    GoogleIdToken idToken = verifier.verify(credential);

    if (idToken == null) {

        // Log ONLY safe diagnostic claims.
        // Do NOT log the complete Google credential/token.
        try {
            String[] parts = credential.split("\\.");

            if (parts.length == 3) {
                String payloadJson =
                        new String(
                                java.util.Base64.getUrlDecoder().decode(parts[1]),
                                java.nio.charset.StandardCharsets.UTF_8
                        );

                com.google.gson.JsonObject payload =
                        com.google.gson.JsonParser.parseString(payloadJson)
                                .getAsJsonObject();

                System.err.println("===== GOOGLE TOKEN DEBUG =====");
                System.err.println("aud = " +
                        (payload.has("aud") ? payload.get("aud").getAsString() : "missing"));
                System.err.println("azp = " +
                        (payload.has("azp") ? payload.get("azp").getAsString() : "missing"));
                System.err.println("iss = " +
                        (payload.has("iss") ? payload.get("iss").getAsString() : "missing"));
                System.err.println("exp = " +
                        (payload.has("exp") ? payload.get("exp").getAsString() : "missing"));
                System.err.println("iat = " +
                        (payload.has("iat") ? payload.get("iat").getAsString() : "missing"));
                System.err.println("==============================");
            }
        } catch (Exception debugException) {
            System.err.println("Could not decode Google token for diagnostics");
        }

        throw new RuntimeException("Invalid Google ID token");
    }

    return idToken.getPayload();
}
}