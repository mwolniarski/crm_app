package pl.wolniarskim.crm_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wolniarskim.crm_app.model.Lead;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
}

