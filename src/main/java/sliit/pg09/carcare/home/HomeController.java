package sliit.pg09.carcare.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        String[][] links = {{"About", "#about"}, {"Contact", "#contact"}, {"Test", "/client"}};
        model.addAttribute("links", links);
        model.addAttribute("imagePath", "/images/home/1.webp");
        return "Home/index";
    }
}