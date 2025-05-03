package sliit.pg09.carcare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final int TOTAL_IMAGES = 4;
    private int currentImageIndex = 2;

    @GetMapping("/")
    public String index(Model model) {
        String[][] links = {{"About", "#about"}, {"Contact", "/contact"}};
        model.addAttribute("links", links);
        model.addAttribute("imagePath", "/images/home/1.webp");
        return "Home/index";
    }

    @GetMapping("/next-image")
    public String getNextImage(Model model) {
        currentImageIndex = (currentImageIndex % TOTAL_IMAGES) + 1;
        model.addAttribute("imagePath", "/images/home/" + currentImageIndex + ".webp");
        return "Home/fragments :: dynamicImage";
    }
}