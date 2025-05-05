package sliit.pg09.carcare.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN c.vehicles v " +
            "WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:vehicleNumber IS NULL OR LOWER(v.license) LIKE LOWER(CONCAT('%', :vehicleNumber, '%')))")
    List<Client> findByCriteria(@Param("name") String name,
                                @Param("email") String email,
                                @Param("vehicleNumber") String vehicleNumber);

    @Query("SELECT DISTINCT c FROM Client c LEFT JOIN c.vehicles v " +
            "WHERE LOWER(c.name) LIKE %:query% OR " +
            "LOWER(c.email) LIKE %:query% OR " +
            "LOWER(v.license) LIKE %:query%")
    List<Client> searchByNameEmailOrVehicle(@Param("query") String query);
}
