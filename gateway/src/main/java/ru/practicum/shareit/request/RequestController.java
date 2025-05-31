package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.NewRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;
    private final String headerCurrentUser = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getMyRequests(@RequestHeader(value = headerCurrentUser) long requestorId) {
        return requestClient.getMyRequests(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getOtherRequests(@RequestHeader(value = headerCurrentUser) long requestorId) {
        return requestClient.getOtherRequests(requestorId);
    }

    @GetMapping("/{request-id}")
    public ResponseEntity<Object> getRequestById(@PathVariable(name = "request-id") long requestId) {
        return requestClient.getRequestById(requestId);
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(value = headerCurrentUser) long requestorId,
                                                @Valid @RequestBody NewRequestDto request) {
        return requestClient.createRequest(requestorId, request);
    }
}
