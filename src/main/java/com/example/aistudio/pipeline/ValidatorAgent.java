package com.example.aistudio.pipeline;

public class ValidatorAgent implements Agent {

    @Override
    public StepName name() {return StepName.VALIDATE;}

    @Override
    public String run(String input, Context ctx) {
        return String.join(
                "\n",
                "Validation checklist:",
                "- Answer is step-by-step.",
                "- Contains at least 1 example and 1 pitfall.",
                "- No excessive theory; keeps practical focus.",
                "- Ends with a small exercise to do next."
        );
    }

}
