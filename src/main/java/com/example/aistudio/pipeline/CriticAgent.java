package com.example.aistudio.pipeline;

public class CriticAgent implements Agent {

    @Override
    public StepName name() {return StepName.CRITIQUE;}

    @Override
    public String run(String input, Context ctx) {
        return String.join(
                "\n",
                "Improvements to apply:",
                "- Add concrete examples for each step (one-liners).",
                "- Keep paragraphs short; prefer lists.",
                "- Explicitly mention edge cases / typical mistakes.",
                "- Add a self-check mini-quiz at the end (3 questions)."
        );
    }

}
