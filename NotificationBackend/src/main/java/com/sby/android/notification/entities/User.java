package com.sby.android.notification.entities;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;

@Entity
public class User {

    @Id
    private UUID id;

    private String deviceToken;

    //@OneToMany
    //private Set<String> topics = new HashSet<>();


    public User() {
    }

    public User(UUID id, String deviceToken, Set<String> topics) {
        this.id = id;
        this.deviceToken = deviceToken;
        //this.topics = topics;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

   /*
   public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }
    */
}

