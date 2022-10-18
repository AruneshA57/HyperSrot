package com.example.srot.data.repository;

import com.example.srot.data.model.Consumer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ConsumerRepository extends CrudRepository<Consumer,Long> {

    Optional<Consumer> findByEmail(String email);
    Optional<Consumer> findByPhoneNumber(String phoneNumber);

}
