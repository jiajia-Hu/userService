package com.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jiajia on 2017/8/4
 */

@Data
@EqualsAndHashCode
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "active")
    private boolean active;
    @Column(name = "password")
    private String password;
    @Column(name = "salt")
    private String salt;
    @Column(name = "phone")
    private String phone;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "groupId")
    private Group group;
}
