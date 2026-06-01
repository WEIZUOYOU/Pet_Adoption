package com.pet.adoption.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "领养申请请求")
public class AdoptionRequest {
    @Schema(description = "宠物ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer petId;
    
    @Schema(description = "申请理由", requiredMode = Schema.RequiredMode.REQUIRED, example = "我很喜欢这只宠物，会好好照顾它")
    private String reason;
    
    @Schema(description = "居住地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京市朝阳区xxx街道")
    private String address;
    
    // getters and setters
    public Integer getPetId() { return petId; }
    public void setPetId(Integer petId) { this.petId = petId; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}