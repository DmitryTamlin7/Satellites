package telemetry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telemetry.domain.InboxRecord;

import java.util.UUID;

@Repository
public interface InboxRepository extends JpaRepository<InboxRecord, UUID> {

}
