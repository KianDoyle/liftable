package com.kd.liftable.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "lifter_names", schema = "open_ipf_db")
public class Name {
    @Id
    @Column(name = "name")
    private String name;

    @Transient
    private String link;

    public Name() {

    }
}
