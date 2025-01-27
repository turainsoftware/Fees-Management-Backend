package io.app.dto;

import io.app.model.Class;
import io.app.model.Gender;
import io.app.model.Months;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentDto {
    private Long id;
    private String name;
    private String phone;
    private String guardianName;
    private String guardianPhone;
    private String email;
    private Gender gender;
    private Months joiningMonth;
    private Class joiningClass;
    private String address;
    private String state;
    private String district;
    private String pinCode;
    private String profilePic;
    private Date createdAt;
    private Date updatedAt;
}
