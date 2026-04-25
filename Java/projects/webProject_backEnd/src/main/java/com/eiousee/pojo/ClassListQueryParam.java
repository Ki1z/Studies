package com.eiousee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassListQueryParam {
    private Integer id = 0;
    private Integer className = 0;
    private Integer classroom = 0;
    private Integer teacherName = 0;
    private Integer startDate = 0;
    private Integer endDate = 0;
    private Integer classStatus = 0;
    private Integer updateTime = 0;
}
