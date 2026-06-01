package com.pet.adoption.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "评论信息")
public class Comment {
    @Schema(description = "评论ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "宠物ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Column(name = "pet_id")
    private Integer petId;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @Column(name = "user_id")
    private Integer userId;

    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "这只宠物真可爱！")
    @Column(columnDefinition = "text")
    private String content;

    @Schema(description = "创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
}