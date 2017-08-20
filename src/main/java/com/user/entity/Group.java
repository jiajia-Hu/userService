package com.user.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jiajia on 2017/8/4
 */
@Entity
@Table(name = "group")
@Data
@EqualsAndHashCode
public class Group {
    @Id
    private Integer id;
    private String groupRole;
}
