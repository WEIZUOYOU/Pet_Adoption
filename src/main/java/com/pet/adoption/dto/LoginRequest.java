package com.pet.adoption.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户登录请求")
public class LoginRequest {
    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "user123")
    private String account;
    
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "password123")
    private String password;
    
    // getters and setters
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}