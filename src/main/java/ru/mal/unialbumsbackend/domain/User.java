package ru.mal.unialbumsbackend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="our_user")
public class User {
//    @Override
//    public String toString() {
//        return "User{" +
//                "role='" + role + '\'' +
//                ", id=" + id +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", login='" + login + '\'' +
//                ", password='" + password + '\'' +
//                ", avatar='" + avatar + '\'' +
//                ", albums=" + albums +
//                '}';
//    }

    @Column(name = "role")
    @Size(max = 100)
    private String role;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @Size(max = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Size(max = 100)
    private String lastName;

    @Column(name="login",nullable = false)
    @Size(max = 100)
    private String login;

    @Column(name="password",nullable = false)
    @Size(max = 100)
    private String password;

    @Column(name="avatar",nullable = false)
    @Size(max = 100)
    private String avatar;

    @OneToMany(mappedBy = "user")
    private List<Album> albums;

}
