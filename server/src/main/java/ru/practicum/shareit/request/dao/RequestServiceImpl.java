package ru.practicum.shareit.request.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestLongDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public List<RequestLongDto> getMyRequests(long requestorId) {
        List<Request> requests = requestRepository.findAllByRequestor_Id(requestorId, Sort.by("created"));
        List<RequestLongDto> requestsDto = RequestMapper.mapToRequestLongDto(requests);
        for (RequestLongDto requestDto : requestsDto) {
            List<Item> items = itemRepository.findAllByRequest_Id(requestDto.getId());
            List<ItemShortDto> itemsDto = ItemMapper.mapToItemShortDto(items);
            requestDto.setItems(itemsDto);
        }
        return requestsDto;
    }

    @Override
    public List<RequestDto> getOtherRequests(long requestorId) {
        List<Request> requests = requestRepository.findAllByRequestor_IdNot(requestorId, Sort.by("created"));
        return RequestMapper.mapToRequestDto(requests);
    }

    @Override
    public RequestLongDto getRequestById(long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException());
        RequestLongDto requestDto = RequestMapper.mapToRequestLongDto(request);
        List<Item> items = itemRepository.findAllByRequest_Id(requestDto.getId());
        List<ItemShortDto> itemsDto = ItemMapper.mapToItemShortDto(items);
        requestDto.setItems(itemsDto);
        return requestDto;
    }

    @Transactional
    @Override
    public RequestDto createRequest(long requestorId, NewRequestDto request) {
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new NotFoundException());
        Request newRequest = requestRepository.save(RequestMapper.mapFromRequest(request, requestor));
        return RequestMapper.mapToRequestDto(newRequest);
    }
}
