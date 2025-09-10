package SpectraSystems.Nexus.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpectraSystems.Nexus.models.TicketPurchase;

@Repository
public interface TicketPurchaseRepository extends JpaRepository<TicketPurchase, Long> {
    // You can add custom query methods if needed
}
