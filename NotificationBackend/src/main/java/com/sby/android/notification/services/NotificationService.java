package com.sby.android.notification.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.sby.android.notification.exceptions.CustomHttpException;
import com.sby.android.notification.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class NotificationService {

    @Value("${google.application.credentials}")
    private String googleApplicationCredentials;

    @Value("${firebase.Database.url}")
    private String firebaseDatabaseUrl;

    private void InitFirebaseApp() {
        if(FirebaseApp.getApps().size() == 0) {
            // Crete Service Account
            InputStream serviceAccount = null;
            try {
                serviceAccount = new ClassPathResource(googleApplicationCredentials).getInputStream();
            } catch (IOException e) {
                throw new NotFoundException("Notification Service Error: Unable to find Google Application Credentials");
            }

            // Create App Options
            FirebaseOptions options;
            try {
                options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .setDatabaseUrl(firebaseDatabaseUrl)
                        .build();
            } catch (IOException e) {
                throw new NotFoundException("Notification Service Error: Unable to find App on Firebase");
            }

            // Initialize and get the default app
            FirebaseApp.initializeApp(options);
        }
    }

    private Message createMessage(String deviceToken){

        // This registration token comes from the client FCM SDKs.
        Notification notification = new Notification( "Notification Title", "This is the body. It will open the Page 2 of the application!");

        return Message.builder()
                .setNotification(notification)
                .putData("OpenPage", "Page2")
                .setToken(deviceToken)
                .build();
    }

    public String sendNotification(String deviceToken) {
        if(deviceToken == null || deviceToken.isEmpty())
            throw new NotFoundException("Notification Service Error: Token is empty");

        // Init
        InitFirebaseApp();

        // Notification message
        Message message = createMessage(deviceToken);

        // Send a message to the device corresponding to the provided registration token.
        String response;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            // Error info: https://firebase.google.com/docs/cloud-messaging/send-message#admin_sdk_error_reference
            throw new CustomHttpException("Cannot send notification: " + e.getMessage() + ": " + e.getErrorCode(), 404);
        }
        return response;
    }
}
