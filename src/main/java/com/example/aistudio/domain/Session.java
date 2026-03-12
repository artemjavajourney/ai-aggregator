package com.example.aistudio.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Session {

    private final String id;
    private String title;
    private final Instant createdAt;
    private Instant lastActivityAt;
    private final List<Message> messages = new ArrayList<>();

    public Session(String id, String title, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.lastActivityAt = createdAt;
    }

    public String getId() {return id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public Instant getCreatedAt() {return createdAt;}

    public Instant getLastActivityAt() {return lastActivityAt;}

    public void setLastActivityAt(Instant lastActivityAt) {this.lastActivityAt = lastActivityAt;}

    public List<Message> getMessages() {return messages;}

}
