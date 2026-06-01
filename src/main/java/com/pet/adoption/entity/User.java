package com.pet.adoption.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息")
public class User {
    @Schema(description = "用户ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "user123")
    @Column(unique = true, nullable = false)
    private String account;

    @Schema(hidden = true)
    @JsonIgnore
    private String password;

    @Schema(description = "昵称", example = "张三")
    private String nickname;
    
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "角色", example = "0")
    @Column(columnDefinition = "int default 0")
    private Integer role;    // 0普通用户 1管理员

    @Schema(description = "状态", example = "0")
    @Column(columnDefinition = "int default 0")
    private Integer status;  // 0正常 1禁用
}