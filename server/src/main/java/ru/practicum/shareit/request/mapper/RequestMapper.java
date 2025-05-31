package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestLongDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class RequestMapper {
    public static RequestDto mapToRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestor(request.getRequestor())
                .created(request.getCreated())
                .build();
    }

    public static List<RequestDto> mapToRequestDto(Iterable<Request> requests) {
        List<RequestDto> requestsResult = new ArrayList<>();

        for (Request request : requests) {
            requestsResult.add(mapToRequestDto(request));
        }

        return requestsResult;
    }

    public static RequestLongDto mapToRequestLongDto(Request request) {
        return RequestLongDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestor(request.getRequestor())
                .created(request.getCreated())
                .build();
    }

    public static List<RequestLongDto> mapToRequestLongDto(Iterable<Request> requests) {
        List<RequestLongDto> requestsResult = new ArrayList<>();

        for (Request request : requests) {
            requestsResult.add(mapToRequestLongDto(request));
        }

        return requestsResult;
    }

    public static Request mapFromRequest(NewRequestDto requestDto, User requestor) {
        return new Request(
                requestDto.getDescription(),
                requestor,
                requestDto.getCreated()
        );
    }
}
