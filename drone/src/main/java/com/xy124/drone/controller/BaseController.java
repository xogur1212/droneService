package com.xy124.drone.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {


    @RequestMapping(value="/")
    public String index(){

        return "/view/index";
    }
}
