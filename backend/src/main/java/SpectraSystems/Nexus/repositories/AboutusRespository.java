package SpectraSystems.Nexus.repositories;

import SpectraSystems.Nexus.models.Aboutus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutusRespository extends JpaRepository<Aboutus, Long> {
    
}
