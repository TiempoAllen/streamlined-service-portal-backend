package com.example.streamlined.backend.Controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            // Add status code to model
            model.addAttribute("statusCode", statusCode);
            
            // Add custom message based on status code
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorMessage", "The page you're looking for doesn't exist.");
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorMessage", "An internal server error occurred.");
            } else {
                model.addAttribute("errorMessage", "An unexpected error occurred.");
            }
        }
        return "error";
    }
}