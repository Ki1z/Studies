package com.sky.vo;

import com.sky.entity.Employee;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "员工分页查询结果")
public class EmployeePageVO implements Serializable {
    @ApiModelProperty("总记录数")
    private Long total;

    @ApiModelProperty("记录列表")
    private List<Employee> records;
}
