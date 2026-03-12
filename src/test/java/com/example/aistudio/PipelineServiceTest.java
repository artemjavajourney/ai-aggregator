package com.example.aistudio;

import com.example.aistudio.service.PipelineService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PipelineServiceTest {

  @Test
  void pipelineProducesStepsAndFinal() {
    var svc = new PipelineService();
    var res = svc.run("s1", "Explain Kafka deeply");
    assertThat(res.finalAnswer()).contains("Topic:");
    assertThat(res.steps()).isNotEmpty();
    assertThat(res.steps().stream().anyMatch(s -> s.name().name().equals("THINK"))).isTrue();
    assertThat(res.steps().stream().anyMatch(s -> s.name().name().equals("FINAL"))).isTrue();
  }
}
