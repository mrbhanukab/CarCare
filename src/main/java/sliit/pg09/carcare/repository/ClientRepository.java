package sliit.pg09.carcare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sliit.pg09.carcare.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
