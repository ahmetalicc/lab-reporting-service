package org.backend.project.reportingapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "laborants")
public class Laborant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstName", length = 50, nullable = false)
    private String firstName;
    @Column(name = "lastName", length = 50, nullable = false)
    private String lastName;
    @Column(name = "hospitalId", length = 7, nullable = false)
    @Size(min = 7, max = 7, message = "Field length must be exactly 7 characters")
    private String hospitalId;

    @OneToMany(mappedBy = "laborant")
    private List<Report> reports;
}
