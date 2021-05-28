package pl.wolniarskim.crm_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.wolniarskim.crm_app.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
