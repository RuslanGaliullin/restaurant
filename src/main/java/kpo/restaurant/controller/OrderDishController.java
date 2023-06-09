package kpo.restaurant.controller;

import jakarta.validation.Valid;
import kpo.restaurant.domain.Dish;
import kpo.restaurant.domain.Order;
import kpo.restaurant.model.OrderDishDTO;
import kpo.restaurant.repos.DishRepository;
import kpo.restaurant.repos.OrderRepository;
import kpo.restaurant.service.OrderDishService;
import kpo.restaurant.util.CustomCollectors;
import kpo.restaurant.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/orderDishs")
public class OrderDishController {

    private final OrderDishService orderDishService;
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;

    public OrderDishController(final OrderDishService orderDishService,
            final OrderRepository orderRepository, final DishRepository dishRepository) {
        this.orderDishService = orderDishService;
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("orderValues", orderRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Order::getId, Order::getStatus)));
        model.addAttribute("dishValues", dishRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Dish::getId, Dish::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("orderDishs", orderDishService.findAll());
        return "orderDish/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("orderDish") final OrderDishDTO orderDishDTO) {
        return "orderDish/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("orderDish") @Valid final OrderDishDTO orderDishDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("dish") && orderDishDTO.getDish() != null && orderDishService.dishExists(orderDishDTO.getDish())) {
            bindingResult.rejectValue("dish", "Exists.orderDish.dish");
        }
        if (bindingResult.hasErrors()) {
            return "orderDish/add";
        }
        orderDishService.create(orderDishDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("orderDish.create.success"));
        return "redirect:/orderDishs";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("orderDish", orderDishService.get(id));
        return "orderDish/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
            @ModelAttribute("orderDish") @Valid final OrderDishDTO orderDishDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final OrderDishDTO currentOrderDishDTO = orderDishService.get(id);
        if (!bindingResult.hasFieldErrors("dish") && orderDishDTO.getDish() != null &&
                !orderDishDTO.getDish().equals(currentOrderDishDTO.getDish()) &&
                orderDishService.dishExists(orderDishDTO.getDish())) {
            bindingResult.rejectValue("dish", "Exists.orderDish.dish");
        }
        if (bindingResult.hasErrors()) {
            return "orderDish/edit";
        }
        orderDishService.update(id, orderDishDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("orderDish.update.success"));
        return "redirect:/orderDishs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
            final RedirectAttributes redirectAttributes) {
        orderDishService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("orderDish.delete.success"));
        return "redirect:/orderDishs";
    }

}
