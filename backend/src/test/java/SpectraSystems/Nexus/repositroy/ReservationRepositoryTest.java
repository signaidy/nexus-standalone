package SpectraSystems.Nexus.repositroy;
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

import SpectraSystems.Nexus.models.Reservation;
import SpectraSystems.Nexus.repositories.ReservationRepository;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReservationRepositoryTest {
    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationRepositoryTest(ReservationRepository reservationRepository){
        this.reservationRepository = reservationRepository;
    }

    @Test
    public void reservationRepository_SaveAll_RetrunSavedReservation(){
        // Arrange
        Reservation reservation = Reservation.builder().userid(45L).hotelId("45").hotel("null").dateStart(new Date()).dateEnd(new Date()).location("Spain").reservationNumber("something").price(20D).guests(20).build();
        //Act
        Reservation savedReservation = reservationRepository.save(reservation);
        //Assert
        Assertions.assertThat(savedReservation).isNotNull();
        Assertions.assertThat(savedReservation.getId() > 0L).isTrue();
    }

    @Test 
    public void reservationRepository_GetAll_ReturnsMoreThanOne(){
        // Arrange
       Reservation reservation = Reservation.builder().userid(45L).hotelId("45").hotel("null").dateStart(new Date()).dateEnd(new Date()).location("Spain").reservationNumber("something").price(20D).guests(20).build();
        
        Reservation reservation2 = Reservation.builder().userid(45L).hotelId("45").hotel("null").dateStart(new Date()).dateEnd(new Date()).location("Spain").reservationNumber("something2").price(20D).guests(20).build();
       //Act
       reservationRepository.save(reservation);
       reservationRepository.save(reservation2);
       List<Reservation> reservations = reservationRepository.findAll();
       //Assert
        Assertions.assertThat(reservations).isNotNull();
       Assertions.assertThat(reservations.size()).isEqualTo(2);

    }

    @Test
    public void reservationRepository_FindById_RetrunSavedRes(){
        // Arrange
       Reservation reservation = Reservation.builder().userid(45L).hotelId("45").hotel("null").dateStart(new Date()).dateEnd(new Date()).location("Spain").reservationNumber("something").price(20D).guests(20).build();
        
        //Act
        reservationRepository.save(reservation);
        Reservation reservationFound = reservationRepository.findById(reservation.getId()).get();
        //Assert
        Assertions.assertThat(reservationFound).isNotNull();
    }

    @Test
    public void reservationRepository_UpdateReservation_RetrunReservation(){
        // Arrange
       Reservation reservation = Reservation.builder().userid(45L).hotelId("45").hotel("null").dateStart(new Date()).dateEnd(new Date()).location("Spain").reservationNumber("something").price(20D).guests(20).build();
        
        //Act
        reservationRepository.save(reservation);
        Reservation reservationFound = reservationRepository.findById(reservation.getId()).get();
        reservationFound.setPrice(8000D);
        reservationFound.setRating(5L);
        Reservation updateReservation  = reservationRepository.save(reservationFound);
        //Assert
        Assertions.assertThat(updateReservation.getPrice()).isNotNull();
        Assertions.assertThat(updateReservation.getRating()).isNotNull();
    }

    @Test 
    public  void reservationRepository_Delete_ReturnsReservationEmpty(){
        //Arrange
       Reservation reservation = Reservation.builder().userid(45L).hotelId("45").hotel("null").dateStart(new Date()).dateEnd(new Date()).location("Spain").reservationNumber("something").price(20D).guests(20).build();
        
        
        //Act
        reservationRepository.save(reservation);

        reservationRepository.deleteById(reservation.getId());
        Optional<Reservation> reservationReturn = reservationRepository.findById(reservation.getId());
        //Assert
        Assertions.assertThat(reservationReturn).isEmpty();

    }
}
