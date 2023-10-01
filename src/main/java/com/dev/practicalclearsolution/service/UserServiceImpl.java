package com.dev.practicalclearsolution.service;

import com.dev.practicalclearsolution.Utils.ValidationUtils;
import com.dev.practicalclearsolution.entity.User;
import com.dev.practicalclearsolution.exception.ValidationException;
import com.dev.practicalclearsolution.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Value("${user.register.age.min}")
    private int minAge;
    private final ValidationUtils validationUtils;


    @Override
    public ResponseEntity<User> save(User user) {
        validationUtils.validate(user);
        LocalDate dateOfUserBirth = user.getDate();
        if (dateOfUserBirth != null) {
            int userAge = Period.between(dateOfUserBirth, LocalDate.now()).getYears();
            if (userAge < minAge) {
                throw ValidationException.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("Age should be " + minAge + "+").build();
            }
        }
        return ResponseEntity.ok().body(userRepository.save(user));
    }

    @SneakyThrows
    @Override
    public ResponseEntity<User> update(long id, User user) {
        User userById = userRepository
                .findById(id)
                .orElseThrow(() -> ValidationException
                        .builder()
                        .message("User with id: " + id + " not found")
                        .httpStatus(HttpStatus.BAD_REQUEST).build());
        userById.setName(user.getName());
        userById.setSurname(user.getSurname());
        userById.setEmail(user.getEmail());
        userById.setDate(user.getDate());
        userById.setAddress(user.getAddress());
        userById.setPhoneNumber(user.getPhoneNumber());
        User changedUser = userRepository.save(userById);
        return ResponseEntity.ok().body(changedUser);
    }

    @Override
    public ResponseEntity<String> delete(long id) {
        User byId = userRepository
                .findById(id)
                .orElseThrow(() -> ValidationException
                        .builder()
                        .message("User with id: " + id + " not found")
                        .httpStatus(HttpStatus.BAD_REQUEST).build());
        userRepository.deleteById(id);
        return ResponseEntity.ok("Success delete user with id: " + id);
    }

    @Override
    public ResponseEntity<List<User>> findAllUsersByBirthdateInRange(
            LocalDate date1, LocalDate date2) {
        if (date1.isAfter(date2)) {
            throw ValidationException
                    .builder()
                    .message("Date 1 should be before Date 2")
                    .httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok()
                .body(userRepository.findAllByDateBetween(date1, date2));
    }
}
