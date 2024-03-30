package pl.dk.ecommerceplatform.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ContactRepository extends JpaRepository<Contact, Long> {
}
