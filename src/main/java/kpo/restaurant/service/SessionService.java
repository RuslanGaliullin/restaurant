package kpo.restaurant.service;

import java.util.List;
import kpo.restaurant.domain.Session;
import kpo.restaurant.domain.User;
import kpo.restaurant.model.SessionDTO;
import kpo.restaurant.repos.SessionRepository;
import kpo.restaurant.repos.UserRepository;
import kpo.restaurant.util.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionService(final SessionRepository sessionRepository,
            final UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public List<SessionDTO> findAll() {
        final List<Session> sessions = sessionRepository.findAll(Sort.by("id"));
        return sessions.stream()
                .map((session) -> mapToDTO(session, new SessionDTO()))
                .toList();
    }

    public SessionDTO get(final Long id) {
        return sessionRepository.findById(id)
                .map((session) -> mapToDTO(session, new SessionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final SessionDTO sessionDTO) {
        final Session session = new Session();
        mapToEntity(sessionDTO, session);
        return sessionRepository.save(session).getId();
    }

    public void update(final Long id, final SessionDTO sessionDTO) {
        final Session session = sessionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(sessionDTO, session);
        sessionRepository.save(session);
    }

    public void delete(final Long id) {
        sessionRepository.deleteById(id);
    }

    private SessionDTO mapToDTO(final Session session, final SessionDTO sessionDTO) {
        sessionDTO.setId(session.getId());
        sessionDTO.setSessionToken(session.getSessionToken());
        sessionDTO.setExpiresAt(session.getExpiresAt());
        sessionDTO.setUser(session.getUser() == null ? null : session.getUser().getId());
        return sessionDTO;
    }

    private Session mapToEntity(final SessionDTO sessionDTO, final Session session) {
        session.setSessionToken(sessionDTO.getSessionToken());
        session.setExpiresAt(sessionDTO.getExpiresAt());
        final User user = sessionDTO.getUser() == null ? null : userRepository.findById(sessionDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        session.setUser(user);
        return session;
    }

}
