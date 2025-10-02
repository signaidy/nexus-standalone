package SpectraSystems.Nexus.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthController {
  @GetMapping({"/healthz", "/actuator/health"})
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("ok");
  }
}