package com.example.aistudio.pipeline;

import java.util.List;

public class ThinkerAgent implements Agent {

    @Override
    public StepName name() {return StepName.THINK;}

    @Override
    public String run(String input, Context ctx) {
        var q = ctx.question();
        var bullets = List.of(
                "Define goal: what the user wants to learn/do.",
                "Give a short mental model (simple analogy).",
                "Provide step-by-step explanation with examples.",
                "Add pitfalls + how to verify understanding.",
                "Finish with a small practice task."
        );

        var sb = new StringBuilder();
        sb.append("Draft plan for: ").append(q).append("\n\n");
        for (int i = 0; i < bullets.size(); i++) {
            sb.append(i + 1).append(") ").append(bullets.get(i)).append("\n");
        }
        sb.append("\nNotes: keep it beginner-friendly, concise, and actionable.");
        return sb.toString();
    }

}
