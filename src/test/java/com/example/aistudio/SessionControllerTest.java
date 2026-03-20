package com.example.aistudio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SessionControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void createListAskFlowWorks() throws Exception {

        String id = createSession();

        mvc.perform(get("/api/sessions"))
           .andExpect(status().isOk());

        mvc.perform(post("/api/sessions/" + id + "/ask")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"question\":\"Hello\"}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.answer").exists())
           .andExpect(jsonPath("$.steps").isArray());

        mvc.perform(get("/api/sessions/" + id + "/messages"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$").isArray());
    }

    @Test
    void validationWorks() throws Exception {
        String id = createSession();
        mvc.perform(post("/api/sessions/" + id + "/ask")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"question\":\"   \"}"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));
    }

    @Test
    void renameSession() throws Exception {
        String id = createSession();

        mvc.perform(patch("/api/sessions/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\":\"New Title\"}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void renameSession_normalizesSpaces() throws Exception {
        String id = createSession();

        mvc.perform(patch("/api/sessions/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\":\"   New Title     \"}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void deleteSession() throws Exception {
        String id = createSession();
        int beforeCount = getSessionCount();

        mvc.perform(delete("/api/sessions/" + id))
           .andExpect(status().isNoContent());

        int afterCount = getSessionCount();

        assertEquals(beforeCount - 1, afterCount);
    }

    @Test
    void deleteSession_notFound_returns404() throws Exception {
        mvc.perform(delete("/api/sessions/" + "not_exist_id"))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.code").value("SESSION_NOT_FOUND"));
    }

    @Test
    void renameSession_blankTitle_returns400() throws Exception {
        String id = createSession();

        mvc.perform(patch("/api/sessions/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\":\"   \"}"))
           .andExpect(status().isBadRequest())
           .andExpect(jsonPath("$.code").value("VALIDATION_FAILED"));

    }

    @Test
    void renameSession_notFound_returns404() throws Exception {
        mvc.perform(patch("/api/sessions/" + "not_exist_id")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"title\":\"New Title\"}"))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.code").value("SESSION_NOT_FOUND"));
    }

    private String createSession() throws Exception {
        var result = mvc.perform(post("/api/sessions")
                                         .contentType(MediaType.APPLICATION_JSON)
                                         .content("{}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").exists())
                        .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                           .get("id")
                           .asText();
    }

    private int getSessionCount() throws Exception {
        var result = mvc.perform(get("/api/sessions"))
                        .andExpect(status().isOk())
                        .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).size();
    }

}
