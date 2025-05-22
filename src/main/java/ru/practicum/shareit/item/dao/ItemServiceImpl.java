package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemView;
import ru.practicum.shareit.item.dto.ItemsView;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<ItemView> findAll(long ownerId) {
        List<Item> items = itemRepository.findAllByOwner_id(ownerId);
        return ItemMapper.mapToItemView(items);
    }

    @Override
    public ItemsView findById(long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        List<Comment> comments = commentRepository.findAllByItem_id(id);
        return ItemMapper.mapToItemsView(item, comments);
    }

    @Override
    public List<ItemView> findByText(String text) {
        if (text == null || text.isBlank())
            return new ArrayList<>();
        else
            return itemRepository.findAll().stream()
                    .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                            | item.getDescription().toLowerCase().contains(text.toLowerCase()))
                    .filter(Item::getAvailable)
                    .map(ItemMapper::mapToItemView)
                    .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ItemView create(long ownerId, ItemRequest item) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException();
        }
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException());
        checkValidation(item);
        Item newItem = itemRepository.save(ItemMapper.mapToNewItem(item, owner));
        return ItemMapper.mapToItemView(newItem);
    }

    @Transactional
    @Override
    public CommentView createComment(long id, long authorId, Comment comment) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException());
        checkValidation(comment, authorId, id);
        Comment newComment = commentRepository.save(CommentMapper.mapToNewComment(comment, author, item));
        return CommentMapper.mapToCommentView(newComment);
    }

    @Transactional
    @Override
    public ItemView update(long id, long ownerId, ItemRequest item) {
        Item oldItem = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (oldItem.getOwner().getId() != ownerId) {
            throw new NotOwnerException();
        }
        Item newItem = ItemMapper.updateItemFields(oldItem, item);
        itemRepository.save(newItem);
        return ItemMapper.mapToItemView(newItem);
    }

    @Transactional
    @Override
    public void remove(long id, long ownerId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (item.getOwner().getId() != ownerId) {
            throw new NotOwnerException();
        }
        itemRepository.deleteById(id);
    }

    private void checkValidation(ItemRequest item) {
        if (item.getName() == null || item.getName().isBlank())
            throw new ValidationException();
        if (item.getDescription() == null || item.getDescription().isBlank())
            throw new ValidationException();
        if (item.getAvailable() == null)
            throw new ValidationException();
    }

    private void checkValidation(Comment comment, long authorId, long itemId) {
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