package com.careers.jobapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Jobs {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Role/Position is mandatory")
    private String position;

    @NotBlank(message = "Company is mandatory")
    private String company;

    @NotNull(message = "Date applied is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate dateApplied;

    @NotNull(message = "Last modified date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate lastModified;

    @NotBlank(message = "Status is mandatory")
    private String status;







}
