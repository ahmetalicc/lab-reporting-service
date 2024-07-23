package org.backend.project.reportingapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Column(name = "firstName", length = 50)
    @NotNull
    private String firstName;
    @Column(name = "lastName", length = 50)
    @NotNull
    private String lastName;
    @Column(name = "hospitalId", length = 7)
    @NotNull
    @Size(min = 7, max = 7, message = "Field length must be exactly 7 characters")
    private String hospitalId;

    @OneToMany(mappedBy = "laborant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Report> reports;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
