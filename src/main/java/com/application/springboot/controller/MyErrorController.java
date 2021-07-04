package com.application.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin(origins = "*")
public class MyErrorController {


//    @GetMapping("/error")
//    public String handleError(HttpServletRequest request){
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if(statusCode == HttpStatus.NOT_FOUND.value()) {
//                return "error404";
//            }
//
//        }
//        return "error404";
//    }
//
//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
}
