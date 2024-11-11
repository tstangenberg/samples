package com.fortytwotalents.samples.pet_clinic.vet;

import com.fortytwotalents.samples.pet_clinic.specialty.Specialty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Vets")
@Getter
@Setter
public class Vet {

  @Id
  @Column(nullable = false, updatable = false)
  @SequenceGenerator(
      name = "primary_sequence",
      sequenceName = "primary_sequence",
      allocationSize = 1,
      initialValue = 10000)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_sequence")
  private Integer id;

  @Column(length = 30)
  private String firstName;

  @Column(length = 30)
  private String lastName;

  @ManyToMany
  @JoinTable(
      name = "VetSpecialties",
      joinColumns = @JoinColumn(name = "vetId"),
      inverseJoinColumns = @JoinColumn(name = "specialtyId"))
  private Set<Specialty> vetSpecialtySpecialties;
}
