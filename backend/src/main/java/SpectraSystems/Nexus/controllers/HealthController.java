package SpectraSystems.Nexus.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nexus")
public class HealthController {
  @GetMapping({"/healthz", "/nexus/healthz"})
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("ok");
  }
}