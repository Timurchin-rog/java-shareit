package ru.practicum.shareit.request.dao;

import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestLongDto;

import java.util.List;

public interface RequestService {
    List<RequestLongDto> getMyRequests(long requestorId);

    List<RequestDto> getOtherRequests(long requestorId);

    RequestLongDto getRequestById(long requestId);

    RequestDto createRequest(long requestorId, NewRequestDto request);


}
