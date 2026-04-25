package com.eiousee.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {
    private Integer id;
    private String username;
    private String name;
    private String token;
}
