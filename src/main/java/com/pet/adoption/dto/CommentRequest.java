package com.pet.adoption.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "评论请求")
public class CommentRequest {
    @Schema(description = "宠物ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer petId;
    
    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "这只宠物真可爱！")
    private String content;
    
    // getters and setters
    public Integer getPetId() { return petId; }
    public void setPetId(Integer petId) { this.petId = petId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}