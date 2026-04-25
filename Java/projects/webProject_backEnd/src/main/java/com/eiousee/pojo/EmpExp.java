package com.eiousee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpExp {
    private Integer id;
    private LocalDate startTime;
    private LocalDate endTime;
    private String company;
    private String job;
}
