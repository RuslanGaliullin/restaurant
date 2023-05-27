package kpo.restaurant.controller;

import jakarta.validation.Valid;
import kpo.restaurant.model.UserDTO;
import kpo.restaurant.service.UserService;
import kpo.restaurant.util.ValidationException;
import kpo.restaurant.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("user") final UserDTO userDTO) {
        return "user/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("user") @Valid final UserDTO userDTO,
                      final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("username") && userService.usernameExists(userDTO.getUsername())) {
            bindingResult.rejectValue("username", "Exists.user.username");
        }
        if (!bindingResult.hasFieldErrors("email") && userService.emailExists(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.user.email");
        }
        if (bindingResult.hasErrors()) {
            return "user/add";
        }
        try {
            userService.create(userDTO);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.create.success"));
        } catch (ValidationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id, final Model model) {
        model.addAttribute("user", userService.get(id));
        return "user/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Integer id,
                       @ModelAttribute("user") @Valid final UserDTO userDTO, final BindingResult bindingResult,
                       final RedirectAttributes redirectAttributes) {
        final UserDTO currentUserDTO = userService.get(id);
        if (!bindingResult.hasFieldErrors("username") &&
                !userDTO.getUsername().equalsIgnoreCase(currentUserDTO.getUsername()) &&
                userService.usernameExists(userDTO.getUsername())) {
            bindingResult.rejectValue("username", "Exists.user.username");
        }
        if (!bindingResult.hasFieldErrors("email") &&
                !userDTO.getEmail().equalsIgnoreCase(currentUserDTO.getEmail()) &&
                userService.emailExists(userDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.user.email");
        }
        if (bindingResult.hasErrors()) {
            return "user/edit";
        }
        try {
            userService.update(id, userDTO);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("user.update.success"));
        } catch (ValidationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Integer id,
                         final RedirectAttributes redirectAttributes) {
        final String referencedWarning = userService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            userService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("user.delete.success"));
        }
        return "redirect:/users";
    }

    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(ValidationException ex) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
