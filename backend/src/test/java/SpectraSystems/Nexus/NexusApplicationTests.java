package SpectraSystems.Nexus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@SpringBootTest
@ActiveProfiles("test") 
@AutoConfigureTestDatabase(replace = Replace.ANY)
class NexusApplicationTests {

    @Test
    void contextLoads() {
    }
}