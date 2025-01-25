package io.app.dto;

import io.app.model.*;
import io.app.model.Class;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TeacherDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String otp = "123456";
    private Date otpExpiry;
    private Gender gender;
    private String profilePic;
    private Role role = Role.TEACHER;
    private Date createdAt;
    private Date updateAt;
    private Set<Subject> subjects;
    private Set<Class> classes;
    private Set<Board> boards;
    private Set<Language> languages;
}
