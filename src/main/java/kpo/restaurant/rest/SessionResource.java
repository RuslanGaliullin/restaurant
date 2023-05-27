package kpo.restaurant.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import jakarta.validation.ValidationException;
import kpo.restaurant.model.SessionDTO;
import kpo.restaurant.service.SessionService;
import kpo.restaurant.util.ErrorResponse;
import kpo.restaurant.util.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/sessions", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionResource {

    private final SessionService sessionService;

    public SessionResource(final SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<SessionDTO>> getAllSessions() {
        return ResponseEntity.ok(sessionService.findAll());
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<SessionDTO> getSession(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(sessionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    @ApiResponse(responseCode = "400", description = "Incorrect input data")
    public ResponseEntity<Long> createSession(@RequestBody @Valid final SessionDTO sessionDTO) {
        final Long createdId = sessionService.create(sessionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", description = "Incorrect input data")
    public ResponseEntity<Void> updateSession(@PathVariable(name = "id") final Long id,
                                              @RequestBody @Valid final SessionDTO sessionDTO) {
        sessionService.update(id, sessionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSession(@PathVariable(name = "id") final Long id) {
        sessionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
