package com.example.srot.business.domain;

import com.example.srot.data.model.Consumer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerProfile {

    private String name;

    private String email;

    private String phoneNumber;

    private LocalDate joiningDate;

    public ConsumerProfile(Consumer entity) {
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.phoneNumber = entity.getPhoneNumber();
        this.joiningDate = entity.getJoiningDate().toLocalDate();
    }
}
