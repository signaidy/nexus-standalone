package SpectraSystems.Nexus.dto;

import SpectraSystems.Nexus.models.User;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
public class JwtAuthenticationResponse {
    private String token;
    private User user;
}
