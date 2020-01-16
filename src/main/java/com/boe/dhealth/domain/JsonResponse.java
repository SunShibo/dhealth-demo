package com.boe.dhealth.domain;

public class JsonResponse<T> {
    private String code;
    private String msg;
    private T data;

    public JsonResponse() {
    }

    public JsonResponse(T data) {
        this.code = "0";
        this.msg = "成功";
        this.data = data;
    }

    public JsonResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return "0".equals(this.code);
    }

    public static JsonResponse<String> success() {
        return new JsonResponse((Object)null);
    }

    public static JsonResponse<String> success(String data) {
        return new JsonResponse(data);
    }

    public static JsonResponse<String> fail() {
        return new JsonResponse("1", "fail");
    }

    public static JsonResponse<String> fail(String msg) {
        return new JsonResponse("1", msg);
    }

    public static JsonResponse<String> fail(String code, String msg) {
        return new JsonResponse(code, msg);
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}