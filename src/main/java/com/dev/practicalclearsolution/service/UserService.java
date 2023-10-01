package com.dev.practicalclearsolution.service;

import com.dev.practicalclearsolution.entity.User;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    //create
    ResponseEntity<User> save(User user);

    //update one or some
    //update all
    ResponseEntity<User> update(long id, User user);

    //delete
    ResponseEntity<?> delete(long id);

    //Search for users by birthdate range
    ResponseEntity<List<User>> findAllUsersByBirthdateInRange(LocalDate date1, LocalDate date2);
}
