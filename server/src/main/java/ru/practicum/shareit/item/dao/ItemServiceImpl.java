package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    @Override
    public List<ItemDto> getItems(long ownerId) {
        List<Item> items = itemRepository.findAllByOwner_id(ownerId);
        return ItemMapper.mapToItemDto(items);
    }

    @Override
    public ItemLongDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException());
        List<Comment> comments = commentRepository.findAllByItem_id(itemId);
        List<Booking> bookings = bookingRepository.findAllByItem_Id(itemId);
        Optional<Booking> lastBooking = bookings.stream()
                .filter(booking -> booking.getEnd().toLocalDate().isBefore(LocalDate.now()))
                .max(Comparator.comparing(Booking::getEnd))
                .stream().findFirst();
        LocalDate lastBookingDate = null;
        if (lastBooking.isPresent())
            lastBookingDate = lastBooking.get().getEnd().toLocalDate();
        Optional<Booking> nextBooking = bookings.stream()
                .filter(booking -> booking.getStart().toLocalDate().isAfter(LocalDate.now()))
                .min(Comparator.comparing(Booking::getStart))
                .stream().findFirst();
        LocalDate nextBookingDate = null;
        if (nextBooking.isPresent())
            nextBookingDate = nextBooking.get().getStart().toLocalDate();
        return ItemMapper.mapToItemLongDto(item, comments, lastBookingDate, nextBookingDate);
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        List<Item> items;
        if (text == null || text.isBlank())
            return new ArrayList<>();
        else {
            items = itemRepository.findAllByText(text);
            return ItemMapper.mapToItemDto(items);
        }
    }

    @Transactional
    @Override
    public ItemDto createItem(long ownerId, NewItemDto item) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException());
        Item newItem = itemRepository.save(ItemMapper.mapFromRequest(item, owner));
        if (item.getRequestId() != null) {
            Request request = requestRepository.findById(item.getRequestId())
                    .orElseThrow(() -> new NotFoundException());
            newItem.setRequest(request);
        }
        return ItemMapper.mapToItemDto(newItem);
    }

    @Transactional
    @Override
    public CommentDto createComment(long itemId, long authorId, NewCommentDto comment) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException());
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException());
        checkValidation(comment, authorId, itemId);
        Comment newComment = commentRepository.save(CommentMapper.mapToNewComment(comment, author, item));
        return CommentMapper.mapToCommentDto(newComment);
    }

    @Transactional
    @Override
    public ItemDto updateItem(long itemId, long ownerId, NewItemDto item) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException());
        if (oldItem.getOwner().getId() != ownerId) {
            throw new NotOwnerException();
        }
        Item newItem = ItemMapper.updateItemFields(oldItem, item);
        itemRepository.save(newItem);
        return ItemMapper.mapToItemDto(newItem);
    }

    @Transactional
    @Override
    public void removeItem(long itemId, long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException());
        if (item.getOwner().getId() != ownerId) {
            throw new NotOwnerException();
        }
        itemRepository.deleteById(itemId);
    }

    private void checkValidation(NewCommentDto comment, long authorId, long itemId) {
        List<Booking> bookings = bookingRepository.findAllByBooker_IdAndItem_Id(authorId, itemId);
        if (bookings.isEmpty())
            throw new ValidationException();
        else {
            boolean isEndBooking = bookings.stream()
                    .anyMatch(booking -> comment.getCreated().isBefore(booking.getEnd()));
            if (isEndBooking)
                throw new ValidationException();
        }
    }
}