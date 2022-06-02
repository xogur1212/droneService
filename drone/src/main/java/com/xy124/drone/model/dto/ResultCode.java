package com.xy124.drone.model.dto;

public enum ResultCode {

    SUCCESS(1,"success"),FAIL(-1,"fail");



    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code=code;
        this.message=message;
    }

    public int getCode(){
        return code;
    }
    public String getMessage(){
        return message;
    }


}
