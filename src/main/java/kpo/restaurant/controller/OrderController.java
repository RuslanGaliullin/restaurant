package kpo.restaurant.controller;

import jakarta.validation.Valid;
import kpo.restaurant.domain.Order;
import kpo.restaurant.domain.User;
import kpo.restaurant.model.OrderDTO;
import kpo.restaurant.repos.UserRepository;
import kpo.restaurant.service.OrderService;
import kpo.restaurant.util.CustomCollectors;
import kpo.restaurant.util.NotFoundException;
import kpo.restaurant.util.ValidationException;
import kpo.restaurant.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(final OrderService orderService, final UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("userValues", userRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getId, User::getUsername)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "order/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("order") final OrderDTO orderDTO) {
        return "order/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("order") @Valid final OrderDTO orderDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "order/add";
        }
        orderService.create(orderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("order.create.success"));
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("order", orderService.get(id));
        return "order/edit";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public String get(@PathVariable final Integer id, final Model model) {
        try {
            OrderDTO result = orderService.get(id);
            model.addAttribute("order", result);
            return String.format("Order %d from user %s. Status: %s", result.getId(), result.getUser(), result.getStatus());
        } catch (NotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage());
        }
    }

    @GetMapping("/cook")
    @ResponseBody
    public String cook() throws InterruptedException {
        orderService.cook();
        return "Processes completed";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
                       @ModelAttribute("order") @Valid final OrderDTO orderDTO,
                       final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "order/edit";
        }
        orderService.update(id, orderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("order.update.success"));
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes) {
        final String referencedWarning = orderService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            orderService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("order.delete.success"));
        }
        return "redirect:/orders";
    }

}
