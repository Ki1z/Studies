package com.eiousee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpQueryParam {
    private String name;
    private String sex;
    @DateTimeFormat(pattern = "yyyy-M-d")
    private LocalDate begin;
    @DateTimeFormat(pattern = "yyyy-M-d")
    private LocalDate end;
    private Integer page = 1;
    private Integer pageSize = 10;
}
