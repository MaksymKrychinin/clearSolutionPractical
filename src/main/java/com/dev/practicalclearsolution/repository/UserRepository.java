package com.dev.practicalclearsolution.repository;

import com.dev.practicalclearsolution.entity.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Override
    User save(User user);
    void deleteById(Long id);

    List<User> findAllByDateBetween(
            @Past(message = "Date should be at past time")
            @NotNull LocalDate date,
            @Past(message = "Date should be at past time")
            @NotNull LocalDate date2);
}
