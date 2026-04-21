package com.eiousee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Integer id;
    private String name;
    private LocalDate birth;
    private String sex;
    private String avatarPath;
    private String deptName;
    private String jobName;
    private LocalDate boardDate;
    private LocalDateTime updateTime;
    // 工作经历
    private List<EmpExp> empExpList;
}
