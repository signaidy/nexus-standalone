package SpectraSystems.Nexus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
  String first_Name;
  String last_Name;
  String email;
  String password;
  Integer age;
  String country;
  String passport;
}
