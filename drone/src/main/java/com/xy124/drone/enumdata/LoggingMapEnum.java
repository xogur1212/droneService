package com.xy124.drone.enumdata;

public enum LoggingMapEnum {

    IN_ACTION(1),POSSIBLE(0),NO_DATA(-1);


    private int code;


    LoggingMapEnum(int code) {
        this.code=code;

    }

    public int getCode(){
        return code;
    }



}