package com.eiousee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassPojo {
    private Integer id;
    private String className;
    private String classroom;
    private String teacherName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String classStatus;
    private LocalDateTime updateTime;
}
