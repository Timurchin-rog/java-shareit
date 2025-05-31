package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dao.RequestService;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestLongDto;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;
    private final String headerCurrentUser = "X-Sharer-User-Id";

    @GetMapping
    public List<RequestLongDto> getMyRequests(@RequestHeader(value = headerCurrentUser) long requestorId) {
        return requestService.getMyRequests(requestorId);
    }

    @GetMapping("/all")
    public List<RequestDto> getOtherRequests(@RequestHeader(value = headerCurrentUser) long requestorId) {
        return requestService.getOtherRequests(requestorId);
    }

    @GetMapping("/{request-id}")
    public RequestLongDto getRequestById(@PathVariable(name = "request-id") long requestId) {
        return requestService.getRequestById(requestId);
    }

    @PostMapping
    public RequestDto createRequest(@RequestHeader(value = headerCurrentUser) long requestorId,
                                    @RequestBody NewRequestDto request) {
        return requestService.createRequest(requestorId, request);
    }
}
