package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Passenger;

import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    Passenger findByEmail(String email);
   // @Query("select p.firstName, p.lastName, p.phoneNumber, p.email, (select count(t) from Ticket t where t.id = p.id) from Passenger p group by p")
    @Query("select p from Passenger p order by p.tickets.size desc, p.email")
    List<Passenger> findAllOrdered();
}
