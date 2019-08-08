package com.sby.android.notification.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.sby.android.notification.exceptions.CustomHttpException;
import com.sby.android.notification.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class NotificationService {

    @Value("${google.application.credentials}")
    private String googleApplicationCredentials;

    @Value("${firebase.Database.url}")
    private String firebaseDatabaseUrl;

    private void InitFirebaseApp() {
        if(FirebaseApp.getApps().size() == 0) {

            // Create App Options
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(getGoogleCredentials())
                    .setDatabaseUrl(firebaseDatabaseUrl)
                    .build();

            // Initialize and get the default app
            FirebaseApp.initializeApp(options);
        }
    }

    private GoogleCredentials getGoogleCredentials() {

        GoogleCredentials googleCredentials;

        try {
            googleCredentials = GoogleCredentials.fromStream(getServiceAccount());
        } catch (IOException e) {
            throw new NotFoundException("Notification Service Error: Unable to find App on Firebase");
        }

        return googleCredentials;
    }

    private InputStream getServiceAccount() {
        // Crete Service Account
        InputStream serviceAccount = null;
        try {
            serviceAccount = new ClassPathResource(googleApplicationCredentials).getInputStream();
        } catch (IOException e) {
            throw new NotFoundException("Notification Service Error: Unable to find Google Application Credentials");
        }
        return serviceAccount;
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

    public String changeSubscription(String deviceToken, String newTopic, String oldTopic) {

        if(deviceToken == null || deviceToken.isEmpty())
            throw new NotFoundException("Notification Service Error: Token is empty");
        if(newTopic == null || newTopic.isEmpty())
            throw new NotFoundException("Notification Service Error: New Topic is empty");
        if(oldTopic == null || oldTopic.isEmpty())
            throw new NotFoundException("Notification Service Error: Old Topic is empty");

        // Init
        InitFirebaseApp();

        String result = unsubscribeToTopic(deviceToken, oldTopic);
        result += '\n' + "--------------------------" + '\n';
        result += subscribeToTopic(deviceToken, newTopic);
        return result;
    }

    private String unsubscribeToTopic(String deviceToken, String topic) {
        // These registration tokens come from the client FCM SDKs.
        List<String> registrationTokens = Arrays.asList(
                deviceToken
        );

        // Subscribe the devices corresponding to the registration tokens to the
        // topic.
        TopicManagementResponse response = null;
        try {
            response = FirebaseMessaging.getInstance().unsubscribeFromTopic(registrationTokens, topic);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

        // See the TopicManagementResponse reference documentation
        // for the contents of response.
        var result = response.getSuccessCount() + " tokens were unsubscribed successfully";
        result += "\n" + response.getFailureCount() + " tokens were in failure to cancel subscription";
        System.out.println(result);

        return result;
    }

    private String subscribeToTopic(String deviceToken, String topic){
        // These registration tokens come from the client FCM SDKs.
        List<String> registrationTokens = Arrays.asList(
                deviceToken
        );

        // Subscribe the devices corresponding to the registration tokens to the topic.
        TopicManagementResponse response = null;
        try {
            response = FirebaseMessaging.getInstance().subscribeToTopic(registrationTokens, topic);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

        // See the TopicManagementResponse reference documentation
        // for the contents of response.
        var result = response.getSuccessCount() + " tokens were subscribed successfully";
        result += "\n" + response.getFailureCount() + " tokens were in failure for subscription";
        System.out.println(result);

        return result;
    }


    public String sendNotificationToTopic(String topicName) {

        // Init
        InitFirebaseApp();

        // Notification message
        Message message = createMessageForTopic(topicName);

        // Send a message to the device corresponding to the provided registration token.
        String response;
        try {
            response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            // Error info: https://firebase.google.com/docs/cloud-messaging/send-message#admin_sdk_error_reference
            throw new CustomHttpException("Cannot send notification: " + e.getMessage() + ": " + e.getErrorCode(), 404);
        }

        // Even if the topic doesn't exist, the answer looks the same as it was a valid topic...
        if(response == ""){
            // TODO
        }

        return "Done!";
    }

    private Message createMessageForTopic(String topicName){

        // This registration token comes from the client FCM SDKs.
        Notification notification = new Notification( "Notification Title", "This is the body. It will open the Page 2 of the application!");

        return Message.builder()
                .setNotification(notification)
                .putData("OpenPage", "Page2")
                .setTopic(topicName)
                .build();
    }


    public String getAnAccessToken() throws IOException {
        return getAccessToken();
    }

    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };
    private String getAccessToken() throws IOException {
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(getServiceAccount())
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }
}
