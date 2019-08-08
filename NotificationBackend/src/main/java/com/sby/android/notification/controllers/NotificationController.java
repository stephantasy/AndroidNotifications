package com.sby.android.notification.controllers;

import com.sby.android.notification.entities.User;
import com.sby.android.notification.services.NotificationService;
import com.sby.android.notification.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RequestMapping("/notification")
@RestController
@Api(tags = {"Notification"})
public class NotificationController {

    private UserService userService;
    private NotificationService notificationService;

    public NotificationController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }


    @RequestMapping(value = "/send/user/", method = RequestMethod.POST)
    @ApiOperation(value = "Send a notification to a specific User")
    public String sendNotificationToUser(@RequestParam UUID recipientId){
        User user = userService.getUserById(recipientId);
        return notificationService.sendNotification(user.getDeviceToken());
    }

    @RequestMapping(value = "/send/token/", method = RequestMethod.POST)
    @ApiOperation(value = "Send a notification to a specific User")
    public String sendNotificationToToken(@RequestParam String deviceToken){
        return notificationService.sendNotification(deviceToken);
    }

    @RequestMapping(value = "/subscribeToTopic/", method = RequestMethod.POST)
    @ApiOperation(value = "Subscribe to a topic")
    public String subscribeToTopic(@RequestParam String deviceToken, @RequestParam String newTopic, @RequestParam String oldTopic){
        return notificationService.changeSubscription(deviceToken, newTopic, oldTopic);
    }


    @RequestMapping(value = "/sendNotificationToTopic/", method = RequestMethod.POST)
    @ApiOperation(value = "Send a notification to all subscribers of a topic")
    public String sendNotificationToTopic(@RequestParam String topicName){
        return notificationService.sendNotificationToTopic(topicName);
    }


    @RequestMapping(value = "/testGet/", method = RequestMethod.GET)
    @ApiOperation(value = "Test a get action")
    public String testGet(){
        return "Get worked!!!";
    }


    @RequestMapping(value = "/getAccessToken/", method = RequestMethod.GET)
    @ApiOperation(value = "Get an access token")
    public String giveMeAnAccessToken(){
        try {
            return notificationService.getAnAccessToken();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Enable to get that...";
        }
    }
}
