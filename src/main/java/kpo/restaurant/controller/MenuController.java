package kpo.restaurant.controller;

import kpo.restaurant.service.MenuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("dishes", menuService.getAvailable().getDishes());
        return "menu/list";
    }
}
