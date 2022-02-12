package project.investmentservice.api;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import project.investmentservice.domain.Hello;

@SessionAttributes("hello")
public class ChannelController {

    @GetMapping("hello/form")
    public String eventForm(Model model) {
        model.addAttribute("hello", new Hello());
        return "/hello/form";
    }


}
