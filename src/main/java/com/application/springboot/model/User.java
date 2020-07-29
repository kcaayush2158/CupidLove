package com.application.springboot.model;
import com.application.springboot.validator.FieldMatch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch.List({
        @FieldMatch(first = "email", second = "confirmEmail", message = "Email does not match"),
        @FieldMatch(first = "password", second = "confirmPassword", message = "Password does not match")
})
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Email(message = "Email is invalid")
    @NotEmpty
    private String email;
    private boolean isActive;
    @Size(message = "Password length should be more than 6 characters",min = 6)
    private String password;
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private AboutMe aboutMe;
    @NotEmpty
    private String username;
    @Temporal(TemporalType.DATE)
    private Date createdDate;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
        @JoinTable( name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Collection<Role> roles;

     @ManyToOne
    private ProfileVisit profile_visit;
    @Temporal(TemporalType.DATE)
    private Date lastConnection;
    private String profilePhoto;
}