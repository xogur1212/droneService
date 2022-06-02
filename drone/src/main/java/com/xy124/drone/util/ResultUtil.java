package com.xy124.drone.util;

import com.xy124.drone.model.dto.Result;
import com.xy124.drone.model.dto.ResultCode;

public  class ResultUtil {


    public static Result makeSuccessResult(ResultCode resultCode) {
        Result sr = new Result();
        sr.setMessage(resultCode.getMessage());
        sr.setCode(resultCode.getCode());
        return sr;
    }

}
