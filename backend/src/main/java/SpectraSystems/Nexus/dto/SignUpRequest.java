package SpectraSystems.Nexus.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class SignUpRequest {
  private String first_Name;
  private String last_Name;
  private String email;
  private String password;
  private Integer age;
  private String country;
  private String passport;
}
