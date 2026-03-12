package com.example.aistudio.pipeline;

public record RunStep(StepName name, String output, long durationMs) {}
