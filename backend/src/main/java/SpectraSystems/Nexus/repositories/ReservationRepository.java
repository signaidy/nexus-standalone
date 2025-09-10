package SpectraSystems.Nexus.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpectraSystems.Nexus.models.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserid(Long userId);
    List<Reservation> findAllByHotelId(String hotelId);
    List<Reservation> findByBundle(String bundle);
    Optional<Reservation> findByReservationNumber(String reservationNumber);
}
