package kpo.restaurant.service;

import java.util.List;
import java.util.regex.Pattern;

import kpo.restaurant.domain.Order;
import kpo.restaurant.domain.Session;
import kpo.restaurant.domain.User;
import kpo.restaurant.model.Role;
import kpo.restaurant.model.UserDTO;
import kpo.restaurant.repos.OrderRepository;
import kpo.restaurant.repos.SessionRepository;
import kpo.restaurant.repos.UserRepository;
import kpo.restaurant.util.ValidationException;
import kpo.restaurant.util.NotFoundException;
import kpo.restaurant.util.WebUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final OrderRepository orderRepository;
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);


    public UserService(final UserRepository userRepository,
                       final SessionRepository sessionRepository, final OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.orderRepository = orderRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map((user) -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer id) {
        return userRepository.findById(id)
                .map((user) -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserDTO userDTO) {
        if (!pattern.matcher(userDTO.getEmail()).matches()) {
            throw new ValidationException("Incorrect email format");
        }
        if (Role.ADMIN.equalsName(userDTO.getRole()) && Role.USER.equalsName(userDTO.getRole())) {
            throw new ValidationException("Incorrect role type: admin or user");
        }
        if (userRepository.existsByUsernameIgnoreCase(userDTO.getUsername())) {
            throw new ValidationException("User with such name already exists");
        }
        if (userRepository.existsByEmailIgnoreCase(userDTO.getEmail())) {
            throw new ValidationException("User with such email already exists");
        }
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final Integer id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        if (!pattern.matcher(userDTO.getEmail()).matches()) {
            throw new ValidationException("Incorrect email format");
        }
        if (Role.ADMIN.equalsName(userDTO.getRole()) && Role.USER.equalsName(userDTO.getRole())) {
            throw new ValidationException("Incorrect role type: admin or user");
        }
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Integer id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPasswordHash());
        userDTO.setRole(user.getRole());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPasswordHash(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setCreatedAt(userDTO.getCreatedAt());
        user.setUpdatedAt(userDTO.getUpdatedAt());
        return user;
    }

    public boolean usernameExists(final String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public String getReferencedWarning(final Integer id) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Session userSession = sessionRepository.findFirstByUser(user);
        if (userSession != null) {
            return WebUtils.getMessage("user.session.user.referenced", userSession.getId());
        }
        final Order userOrder = orderRepository.findFirstByUser(user);
        if (userOrder != null) {
            return WebUtils.getMessage("user.order.user.referenced", userOrder.getId());
        }
        return null;
    }

}
