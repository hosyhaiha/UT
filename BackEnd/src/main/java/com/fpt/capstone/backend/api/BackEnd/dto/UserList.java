package com.fpt.capstone.backend.api.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
public class UserList {
    private Integer value;
    private String label;
    @JsonIgnore
    private String userName;
    @JsonIgnore
    private String rollNumber;

    public UserList(Integer value, String userName, String rollNumber) {
        this.value = value;
        this.userName = userName;
        this.rollNumber = rollNumber;
        this.label = formatUserString(userName, rollNumber);
    }

    public String formatUserString(String fullName, String rollNumber) {
        String name = "";
        if (ObjectUtils.isEmpty(fullName) || fullName == null) {
            fullName = "";
        }
        if (ObjectUtils.isEmpty(rollNumber) || rollNumber == null) {
            rollNumber = "";
        }

        List<String> splitString = Arrays.asList(fullName.split(" "));

        name += splitString.get(splitString.size() - 1).substring(0, 1).toUpperCase() + splitString.get(splitString.size() - 1).substring(1).toLowerCase();

        for (Integer i = 0; i < splitString.size() - 1; i++) {

            name += splitString.get(i).substring(0, 1).toUpperCase();
        }
        return name + rollNumber;
    }
}
