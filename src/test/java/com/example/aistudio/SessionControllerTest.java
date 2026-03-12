package com.example.aistudio;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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
    void deleteSession() throws Exception {

        String id = createSession();

        int beforeCount = getSessionCount();

        mvc.perform(delete("/api/sessions/" + id))
           .andExpect(status().isNoContent());

        int afterCount = getSessionCount();


        assertEquals(beforeCount - 1, afterCount);
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
        var result = mvc.perform(get("/api/sessions").contentType(MediaType.APPLICATION_JSON).content("{}"))
                        .andExpect(status().isOk())
                        .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).size();
    }

}
