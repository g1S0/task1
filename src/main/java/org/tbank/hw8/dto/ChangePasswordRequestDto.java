package org.tbank.hw8.dto;

import lombok.Data;

@Data
public class ChangePasswordRequestDto {
    private String code;
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
