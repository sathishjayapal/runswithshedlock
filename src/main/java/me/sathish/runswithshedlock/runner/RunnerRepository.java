package me.sathish.runswithshedlock.runner;

import org.springframework.data.jpa.repository.JpaRepository;


public interface RunnerRepository extends JpaRepository<Runner, Long> {

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

}
