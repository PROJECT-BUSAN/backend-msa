package project.investmentservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Swagger {
    
    @GetMapping("/api/v1/investment/swagger")
    public String swaggerDocs() {
        return "redirect:/swagger-ui/index.html";
    }
}
