package SpectraSystems.Nexus.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpectraSystems.Nexus.models.Provider;
import SpectraSystems.Nexus.models.Type;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findByType(Type type);
}

