package com.example.srot.data.repository;

import com.example.srot.data.model.EmailCode;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EmailCodeRepository extends CrudRepository<EmailCode, Long> {

    Optional<EmailCode> findByCode(String code);

}
