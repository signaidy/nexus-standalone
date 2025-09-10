package SpectraSystems.Nexus.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpectraSystems.Nexus.models.Comment;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByFlightId(Long flightId);
}

