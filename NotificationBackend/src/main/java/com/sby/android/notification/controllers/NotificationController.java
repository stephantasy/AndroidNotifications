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
    public String subscribeToTopic(){
        return "";
    }



    @RequestMapping(value = "/testGet/", method = RequestMethod.GET)
    @ApiOperation(value = "Test a get action")
    public String testGet(){
        return "Get worked!!!";
    }

}
