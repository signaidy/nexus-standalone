package SpectraSystems.Nexus.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpectraSystems.Nexus.models.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByUserid(Long userId);
    List<Flight> findAllByFlightNumber(String flightNumber);
    List<Flight> findByBundle(String bundle);
}

