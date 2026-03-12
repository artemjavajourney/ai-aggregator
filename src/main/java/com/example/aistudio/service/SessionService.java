package com.example.aistudio.service;

import com.example.aistudio.domain.Message;
import com.example.aistudio.domain.Session;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final Map<String, Session> store = new ConcurrentHashMap<>();

    public List<Session> list() {
        var sessions = new ArrayList<>(store.values());
        sessions.sort(Comparator.comparing(Session::getLastActivityAt).reversed());
        return sessions;
    }

    public Session create() {
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();
        var session = new Session(id, "New session", now);
        store.put(id, session);
        return session;
    }

    public Session getOrThrow(String id) {
        var s = store.get(id);
        if (s == null) {
            throw new NotFoundException("SESSION_NOT_FOUND", "Session not found: " + id);
        }
        return s;
    }

    public void addMessage(String sessionId, Message message) {
        var s = getOrThrow(sessionId);
        s.getMessages().add(message);
        s.setLastActivityAt(Instant.now());
        if (s.getMessages().size() == 1 && message.getRole() == Message.Role.USER) {
            s.setTitle(trimTo(message.getText(), 48));
        }
    }

    private static String trimTo(String s, int max) {
        var t = s.trim().replaceAll("\\s+", " ");
        return t.length() <= max ? t : t.substring(0, max - 1) + "…";
    }

    public Session rename(String sessionId, @NotBlank String title) {
        var s = getOrThrow(sessionId);
        s.setTitle(trimTo(title, 48));
        s.setLastActivityAt(Instant.now());
        return s;
    }

    public void delete(String sessionId) {
        getOrThrow(sessionId);
        store.remove(sessionId);
    }

    public static class NotFoundException extends RuntimeException {

        private final String code;

        public NotFoundException(String code, String message) {
            super(message);
            this.code = code;
        }

        public String getCode() {return code;}

    }

}
