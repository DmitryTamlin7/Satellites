package satellite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import satellite.domain.OutboxRecord;

import java.util.List;
import java.util.UUID;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxRecord, UUID> {
    List<OutboxRecord> findByStatusOrderByCreatedAtAsc(String status);
}
