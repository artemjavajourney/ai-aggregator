package com.example.aistudio.service;

import com.example.aistudio.pipeline.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PipelineService {

    private final List<Agent> agents;

    public PipelineService() {
        this.agents = List.of(
                new ThinkerAgent(),
                new CriticAgent(),
                new ValidatorAgent()
        );
    }

    public RunResult run(String sessionId, String question) {
        var runId = UUID.randomUUID().toString();
        var ctx = new Agent.Context(sessionId, question);

        var steps = new ArrayList<RunStep>();
        String current = "";

        for (Agent agent : agents) {
            long start = System.nanoTime();
            String out = agent.run(current, ctx);
            long durationMs = (System.nanoTime() - start) / 1_000_000L;
            steps.add(new RunStep(agent.name(), out, durationMs));
            current = out;
        }

        String finalAnswer = assembleFinalAnswer(question, steps);
        steps.add(new RunStep(StepName.FINAL, finalAnswer, 0));

        return new RunResult(runId, finalAnswer, steps);
    }

    private String assembleFinalAnswer(String question, List<RunStep> steps) {
        String plan = steps.stream()
                           .filter(s -> s.name() == StepName.THINK)
                           .findFirst()
                           .map(RunStep::output)
                           .orElse("");

        String critique = steps.stream()
                               .filter(s -> s.name() == StepName.CRITIQUE)
                               .findFirst()
                               .map(RunStep::output)
                               .orElse("");

        String validate = steps.stream()
                               .filter(s -> s.name() == StepName.VALIDATE)
                               .findFirst()
                               .map(RunStep::output)
                               .orElse("");

        return "Topic: " + question + "\n\n"
                + "1) Mental model\n"
                + "Think of it as a conveyor belt: define a goal -> break into steps -> verify each step.\n\n"
                + "2) Step-by-step guide\n"
                + plan + "\n\n"
                + "3) Make it better (critic notes)\n"
                + critique + "\n\n"
                + "4) Self-check (validator)\n"
                + validate + "\n\n"
                + "5) Small exercise\n"
                + "Pick one subtopic and write 5 bullet points: definition, example, pitfall, how to debug, how to test.\n";
    }

}
