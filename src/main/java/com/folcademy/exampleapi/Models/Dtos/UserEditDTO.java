package com.folcademy.exampleapi.Models.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDTO {
    String name;
    String surname;
    String email;
    String password;
}
