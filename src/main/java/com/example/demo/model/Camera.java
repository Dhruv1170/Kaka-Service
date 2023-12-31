package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String camName;

    private String camDes;

    @ManyToOne
    private Zone zone;
}
