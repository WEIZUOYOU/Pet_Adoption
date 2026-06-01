package com.pet.adoption.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "统一响应结果")
public class Result {
    @Schema(description = "响应码", example = "200")
    private int code;
    
    @Schema(description = "响应消息", example = "操作成功")
    private String msg;
    
    @Schema(description = "响应数据")
    private Object data;

    public Result() {}

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success() {
        return new Result(200, "操作成功", null);
    }

    public static Result success(Object data) {
        return new Result(200, "操作成功", data);
    }

    public static Result success(String msg, Object data) {
        return new Result(200, msg, data);
    }

    public static Result error(int code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result error(String msg) {
        return new Result(500, msg, null);
    }

    // getters and setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}