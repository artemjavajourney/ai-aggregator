package com.example.aistudio.domain;

import java.time.Instant;

public class Message {

    public enum Role {USER, AI}

    private final String id;
    private final Role role;
    private final String text;
    private final Instant createdAt;

    public Message(String id, Role role, String text, Instant createdAt) {
        this.id = id;
        this.role = role;
        this.text = text;
        this.createdAt = createdAt;
    }

    public String getId() {return id;}

    public Role getRole() {return role;}

    public String getText() {return text;}

    public Instant getCreatedAt() {return createdAt;}

}
