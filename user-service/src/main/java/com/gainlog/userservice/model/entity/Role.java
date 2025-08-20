package com.gainlog.userservice.model.entity;

import com.gainlog.userservice.utils.ERole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Size(max = 20)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, length = 20)
    private ERole name;
}