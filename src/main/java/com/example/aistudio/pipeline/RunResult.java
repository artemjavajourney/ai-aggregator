package com.example.aistudio.pipeline;

import java.util.List;

public record RunResult(String runId, String finalAnswer, List<RunStep> steps) {}
