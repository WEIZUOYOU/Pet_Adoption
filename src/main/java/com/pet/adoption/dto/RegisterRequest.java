package com.pet.adoption.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户注册请求")
public class RegisterRequest {
    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "user123")
    private String account;
    
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "password123")
    private String password;
    
    @Schema(description = "昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String nickname;
    
    @Schema(description = "手机号", example = "13800138000")
    private String phone;
    
    // getters and setters
    public String getAccount() { return account; }
    public void setAccount(String account) { this.account = account; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}