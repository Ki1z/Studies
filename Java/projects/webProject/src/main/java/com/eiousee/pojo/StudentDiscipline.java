package com.eiousee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDiscipline {
    private Integer studentId;
    private LocalDate disciplineDate;
    private String disciplineReason;
    private Float deductionPoints;
}
