package com.example.aistudio.web;

import com.example.aistudio.domain.Message;
import com.example.aistudio.domain.Session;
import com.example.aistudio.service.PipelineService;
import com.example.aistudio.service.SessionService;
import com.example.aistudio.web.dto.Dtos;
import com.example.aistudio.web.dto.Dtos.SessionSummary;
import com.example.aistudio.web.dto.request.RenameSessionRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final PipelineService pipelineService;

    public SessionController(SessionService sessionService, PipelineService pipelineService) {
        this.sessionService = sessionService;
        this.pipelineService = pipelineService;
    }

    @GetMapping
    public List<Dtos.SessionSummary> list() {
        return sessionService.list().stream()
                             .map(s -> new Dtos.SessionSummary(
                                     s.getId(),
                                     s.getTitle(),
                                     s.getCreatedAt().toString(),
                                     s.getLastActivityAt().toString()
                             ))
                             .toList();
    }

    @PostMapping
    public Dtos.SessionSummary create() {
        return toSummary(sessionService.create());
    }

    @GetMapping("/{id}/messages")
    public List<Dtos.MessageDto> messages(@PathVariable String id) {
        var s = sessionService.getOrThrow(id);
        return s.getMessages().stream()
                .map(m -> new Dtos.MessageDto(m.getId(), m.getRole().name(), m.getText(), m.getCreatedAt().toString()))
                .toList();
    }

    @PostMapping("/{id}/ask")
    public Dtos.AskResponse ask(@PathVariable String id, @Valid @RequestBody Dtos.AskRequest req) {
        sessionService.addMessage(id, new Message(UUID.randomUUID().toString(), Message.Role.USER, req.question(), Instant.now()));
        var run = pipelineService.run(id, req.question());
        sessionService.addMessage(id, new Message(UUID.randomUUID().toString(), Message.Role.AI, run.finalAnswer(), Instant.now()));
        return new Dtos.AskResponse(id, run.runId(), run.finalAnswer(), run.steps());
    }

    @PatchMapping("/{id}")
    public Dtos.SessionSummary rename(@PathVariable String id, @Valid @RequestBody RenameSessionRequest req) {
        return toSummary(sessionService.rename(id, req.title()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private Dtos.SessionSummary toSummary(Session s) {
        return new SessionSummary(
                s.getId(),
                s.getTitle(),
                s.getCreatedAt().toString(),
                s.getLastActivityAt().toString()
        );
    }

}
