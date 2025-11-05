package com.apex_auto_mode_garage.apexAutoModeGarage.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserModel {
    private String id;
    private String userName;
    private String password;
    private String email;
    private String role = "USER";

    public UserModel(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public UserModel(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
