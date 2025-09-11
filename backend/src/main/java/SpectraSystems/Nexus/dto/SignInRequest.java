package SpectraSystems.Nexus.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class SignInRequest {
  private String email;
  private String password;
}