package project.investmentservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Swagger {
    
    @GetMapping("/swagger/investment")
    public String swaggerDocs() {
        return "redirect:/swagger-ui/index.html";
    }
}
