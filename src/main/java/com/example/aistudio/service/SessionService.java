package com.example.aistudio.service;

import com.example.aistudio.domain.Message;
import com.example.aistudio.domain.Session;
import com.example.aistudio.entity.SessionEntity;
import com.example.aistudio.repository.MessageRepository;
import com.example.aistudio.repository.SessionRepository;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final MessageRepository messageRepository;

    SessionService(SessionRepository sessionRepository, MessageRepository messageRepository) {
        this.sessionRepository = sessionRepository;
        this.messageRepository = messageRepository;
    }

    public List<SessionEntity> list() {
        List<SessionEntity> sessions = sessionRepository.findAll();
        sessions.sort(Comparator.comparing(SessionEntity::getLastActivityAt).reversed());
        return sessions;
    }

    public SessionEntity create() {
        return sessionRepository.save(new SessionEntity("New session"));
    }

    public Optional<SessionEntity> getOrThrow(String id) {
        var s = sessionRepository.findById(UUID.fromString(id));
        if (s.isEmpty()) {
            throw new NotFoundException("SESSION_NOT_FOUND", "Session not found: " + id);
        }
        return s;
    }

    public void addMessage(String sessionId, Message message) {
        var s = getOrThrow(sessionId);
        messageRepository.
        s.getMessages().add(message);
        s.setLastActivityAt(Instant.now());
        if (s.getMessages().size() == 1 && message.getRole() == Message.Role.USER) {
            s.setTitle(trimTo(message.getText(), 48));
        }
    }

    public Session rename(String sessionId, String title) {
        var s = getOrThrow(sessionId);
        s.setTitle(normalizeTitle(title));
        s.setLastActivityAt(Instant.now());
        return s;
    }

    public void delete(String sessionId) {
        getOrThrow(sessionId);
        store.remove(sessionId);
    }

    private static String trimTo(String s, int max) {
        var t = s.trim().replaceAll("\\s+", " ");
        return t.length() <= max ? t : t.substring(0, max - 1) + "…";
    }

    private static String normalizeTitle(String title) {
        return title.trim().replaceAll("\\s+", " ");
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
