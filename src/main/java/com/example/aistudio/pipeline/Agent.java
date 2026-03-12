package com.example.aistudio.pipeline;

public interface Agent {

    StepName name();

    String run(String input, Context ctx);

    record Context(String sessionId, String question) {}

}
