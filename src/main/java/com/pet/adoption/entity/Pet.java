package com.pet.adoption.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "pet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "宠物信息")
public class Pet {
    @Schema(description = "宠物ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "宠物名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "小花")
    private String name;
    
    @Schema(description = "物种", requiredMode = Schema.RequiredMode.REQUIRED, example = "猫")
    private String species;   // 猫/狗/其他
    
    @Schema(description = "品种", example = "布偶猫")
    private String breed;     // 品种
    
    @Schema(description = "年龄", example = "2")
    private Integer age;
    
    @Schema(description = "图片路径", example = "/images/pet1.jpg")
    private String image;     // 图片路径
    
    @Schema(description = "描述", example = "一只可爱的布偶猫")
    @Column(columnDefinition = "text")
    private String description;
    
    @Schema(description = "健康状况", example = "健康")
    private String health;    // 健康状况

    @Schema(description = "状态", example = "待领养")
    @Column(columnDefinition = "varchar(20) default '待领养'")
    private String status;    // 待领养/已领养/下架

    @Schema(description = "添加时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addTime;
}