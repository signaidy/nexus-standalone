package SpectraSystems.Nexus.repositroy;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import SpectraSystems.Nexus.models.Flight;
import SpectraSystems.Nexus.repositories.FlightRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FlightRepositoryTest {
    
    private FlightRepository flightRepositoryTest;

    @Autowired
    public FlightRepositoryTest(FlightRepository flightRepositoryTest){
        this.flightRepositoryTest = flightRepositoryTest;
    }

    @Test
    public void flightRepository_SaveAll_RetrunSavedFlight(){
        // Arrange
        Flight flight = Flight.builder().userid(45L).flightNumber("45").departureDate(new Date()).departureLocation("Spain").purchaseDate(LocalDate.now()).arrivalLocation("Guatemala").type("premium").price(20D).build();
        //Act
        Flight savedFlight = flightRepositoryTest.save(flight);
        //Assert
        Assertions.assertThat(savedFlight).isNotNull();
        Assertions.assertThat(savedFlight.getId() > 0L).isTrue();
    }

    @Test 
    public void flightRepository_GetAll_ReturnsMoreThanOneFlight(){
        // Arrange
        Flight flight = Flight.builder().userid(45L).flightNumber("45").departureDate(new Date()).departureLocation("Spain").purchaseDate(LocalDate.now()).arrivalLocation("Guatemala").type("premium").price(20D).build();
        Flight flight2 = Flight.builder().userid(45L).flightNumber("45").departureDate(new Date()).departureLocation("Spain").purchaseDate(LocalDate.now()).arrivalLocation("Guatemala").type("premium").price(20D).build();
       //Act
       flightRepositoryTest.save(flight);
       flightRepositoryTest.save(flight2);
       List<Flight> flights = flightRepositoryTest.findAll();
       //Assert
        Assertions.assertThat(flights).isNotNull();
       Assertions.assertThat(flights.size()).isEqualTo(2);

    }

    @Test
    public void flightRepository_FindById_RetrunSavedFlight(){
        // Arrange
        Flight flight = Flight.builder().userid(45L).flightNumber("45").departureDate(new Date()).departureLocation("Spain").purchaseDate(LocalDate.now()).arrivalLocation("Guatemala").type("premium").price(20D).build();
        //Act
        flightRepositoryTest.save(flight);
        Flight flightFound = flightRepositoryTest.findById(flight.getId()).get();
        //Assert
        Assertions.assertThat(flightFound).isNotNull();
    }

    @Test
    public void flightRepository_UpdateFlight_RetrunFlight(){
        // Arrange
        Flight flight = Flight.builder().userid(45L).flightNumber("45").departureDate(new Date()).departureLocation("Spain").purchaseDate(LocalDate.now()).arrivalLocation("Guatemala").type("premium").price(20D).build();
        //Act
        flightRepositoryTest.save(flight);
        Flight flightFound = flightRepositoryTest.findById(flight.getId()).get();
        flightFound.setPrice(8000D);
        flightFound.setRating(5L);
        Flight updateFlight  = flightRepositoryTest.save(flightFound);
        //Assert
        Assertions.assertThat(updateFlight.getPrice()).isNotNull();
        Assertions.assertThat(updateFlight.getRating()).isNotNull();
    }

    @Test 
    public  void flightRepository_Delete_ReturnsFlihgtEmpty(){
        //Arrange
        Flight flight = Flight.builder().userid(45L).flightNumber("45").departureDate(new Date()).departureLocation("Spain").purchaseDate(LocalDate.now()).arrivalLocation("Guatemala").type("premium").price(20D).build();
       
        //Act
        flightRepositoryTest.save(flight);

        flightRepositoryTest.deleteById(flight.getId());
        Optional<Flight> flightReturn = flightRepositoryTest.findById(flight.getId());
        //Assert
        Assertions.assertThat(flightReturn).isEmpty();

    }
}
