package io.app.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class Teacher implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true, nullable = true)
    private String phone;
    @Column(unique = true, nullable = true)
    private String email;
    private String otp = "123456";
    private Date otpExpiry;
    private Gender gender;
    private String profilePic;
    private Role role = Role.TEACHER;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @ManyToMany
    @JoinTable(
            name = "TeacherSubjects",
            joinColumns = @JoinColumn(name = "teacherId"),
            inverseJoinColumns = @JoinColumn(name = "subjectId")
    )
    private Set<Subject> subjects=new HashSet<>();

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "teacherId"),
            inverseJoinColumns = @JoinColumn(name = "classId"))
    private Set<Class> classes=new HashSet<>();

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "teacherId"),
            inverseJoinColumns = @JoinColumn(name = "boardId"))
    private Set<Board> boards=new HashSet<>();

    @ManyToMany
    @JoinTable(joinColumns = @JoinColumn(name = "teacherId"),
            inverseJoinColumns = @JoinColumn(name = "languageId"))
    private Set<Language> languages;

    @OneToMany(mappedBy = "teacher")
    @ToString.Exclude
    private Set<Batch> batches = new HashSet<>();


    @PrePersist
    public void preCreate() {
        createdAt = new Date();
        updateAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updateAt = new Date();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role.name()));
        return roles;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phone); // Use immutable fields only
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) &&
                Objects.equals(name, teacher.name) &&
                Objects.equals(phone, teacher.phone); // Use immutable fields only
    }
}
