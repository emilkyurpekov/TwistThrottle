package twistthrottle.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
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
        Integer statusCode = null;
        String statusMessage = "Something went wrong";
        String errorMessage = "An unexpected error occurred.";

        if (status != null) {
            statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                statusMessage = "Page Not Found (404)";
                errorMessage = "The page you are looking for does not exist.";
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                statusMessage = "Internal Server Error (500)";
                errorMessage = "There was a problem processing your request.";
            } else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                statusMessage = "Forbidden (403)";
                errorMessage = "You do not have permission to access this resource.";
            }
            else {
                statusMessage = "Error " + statusCode;
            }
        }


        model.addAttribute("statusCode", statusCode != null ? statusCode : "N/A");
        model.addAttribute("statusMessage", statusMessage);
        model.addAttribute("errorMessage", errorMessage);

        return "error";
    }


}