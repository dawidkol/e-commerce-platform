package pl.dk.ecommerceplatform.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByName(String name);
}
