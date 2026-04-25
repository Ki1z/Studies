package com.eiousee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StuQueryParam {
    private String name;
    private String educationName;
    private String className;
    private Integer page = 1;
    private Integer pageSize = 10;
}
