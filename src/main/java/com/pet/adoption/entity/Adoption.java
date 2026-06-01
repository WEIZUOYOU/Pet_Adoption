package com.pet.adoption.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "adoption")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "领养申请信息")
public class Adoption {
    @Schema(description = "申请ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "宠物ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Column(name = "pet_id")
    private Integer petId;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Column(name = "user_id")
    private Integer userId;

    @Schema(description = "申请理由", requiredMode = Schema.RequiredMode.REQUIRED, example = "我很喜欢这只宠物，会好好照顾它")
    @Column(columnDefinition = "text")
    private String reason;

    @Schema(description = "居住地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京市朝阳区xxx街道")
    private String address;

    @Schema(description = "申请状态", example = "待审核")
    @Column(columnDefinition = "varchar(20) default '待审核'")
    private String status;   // 待审核/通过/驳回

    @Schema(description = "申请时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyTime;

    @Schema(description = "审核时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date auditTime;
}