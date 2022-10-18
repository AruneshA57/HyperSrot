package com.example.srot.business.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailTemplate {

    private String destination;
    private String templateName;
    private Map<String,String> templateVariables;

    @Override
    public String toString() {
        return "EmailTemplate{" +
                "destination='" + destination + '\'' +
                ", templateName='" + templateName + '\'' +
                ", templateVariables=" + templateVariables +
                '}';
    }
}
