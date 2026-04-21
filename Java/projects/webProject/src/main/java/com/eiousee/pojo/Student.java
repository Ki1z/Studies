package com.eiousee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String sex;
    private LocalDate birth;
    private String className;
    private String majorName;
    private String educationName;
    private LocalDateTime updateTime;
    private Integer DisciplineCount;
    private Float DeductionPoints = 0.0f;
}
