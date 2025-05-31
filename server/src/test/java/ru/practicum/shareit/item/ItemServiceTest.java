package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dao.ItemServiceImpl;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemService")
public class ItemServiceTest extends ItemInit {

    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    RequestRepository requestRepository;

    ItemServiceImpl itemService;

    @BeforeEach
    void init() {
        itemService = new ItemServiceImpl(
                itemRepository,
                userRepository,
                commentRepository,
                bookingRepository,
                requestRepository
        );
    }

    @Test
    @DisplayName("Должен получать вещи")
    void shouldGetItems() {
        Mockito.when(itemRepository.findAllByOwner_id(userId)).thenReturn(List.of(itemWithId));

        List<ItemDto> itemsDtoFromService = itemService.getItems(userId);
        List<ItemDto> itemsDto = List.of(itemDto);

        Assertions.assertEquals(itemsDto, itemsDtoFromService, "Списки вещей не совпадают при получении");
    }

    @Test
    @DisplayName("Должен находить вещь по id")
    void shouldFindItemById() {
        ItemLongDto itemLongDto = ItemLongDto.builder()
                .id(itemId)
                .name("Дрель")
                .description("Работает от сети, 1300 Вт")
                .owner(user)
                .available(true)
                .build();

        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemWithId));

        Comment comment = new Comment(
                1L,
                "Хорошая вещь",
                user,
                itemWithId
        );

        List<Comment> comments = List.of(comment);
        Mockito
                .when(commentRepository.findAllByItem_id(anyLong()))
                .thenReturn(comments);
        itemLongDto.setComments(comments);

        Booking booking = new Booking(
                1L,
                itemWithId,
                LocalDateTime.now().minusYears(1),
                LocalDateTime.now().plusYears(1),
                user,
                BookingState.APPROVED
        );

        List<Booking> bookings = List.of(booking);
        Mockito
                .when(bookingRepository.findAllByItem_Id(anyLong()))
                .thenReturn(bookings);

        ItemLongDto itemDtoFromService = itemService.getItemById(itemId);

        Assertions.assertEquals(itemLongDto, itemDtoFromService, "Вещи не совпадают при поиске по id");
    }

    @Test
    @DisplayName("Должен находить вещи по тексту")
    void shouldFindItemsByText() {
        String text = "Дрель";
        Mockito
                .when(itemRepository.findAllByText(text))
                .thenReturn(List.of(itemWithId));

        List<ItemDto> itemsDtoFromService = itemService.getItemsByText(text);
        List<ItemDto> itemsDto = List.of(itemDto);

        Assertions.assertEquals(itemsDto, itemsDtoFromService, "Списки вещей не совпадают при поиске по тексту");
    }

    @Test
    @DisplayName("Должен создавать вещь по запросу")
    void shouldCreateItemByRequest() {
        long requestId = 1;
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(itemWithId);

        newItemDto.setRequestId(requestId);
        Request request = new Request(
                requestId,
                "Нужна дрель",
                user
        );
        Mockito
                .when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.of(request));

        ItemDto itemDtoFromService = itemService.createItem(userId, newItemDto);
        Assertions.assertEquals(itemDto, itemDtoFromService, "Вещи не совпадают при создании");
    }

    @Test
    @DisplayName("Должен создавать комментарий")
    void shouldCreateComment() {
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemWithId));

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));

        long commentId = 1;
        Comment comment = new Comment(
                1L,
                "Хорошая вещь",
                user,
                itemWithId
        );
        Mockito
                .when(commentRepository.save(any()))
                .thenReturn(comment);

        NewCommentDto newCommentDto = NewCommentDto.builder()
                .text("Хорошая вещь")
                .created(LocalDateTime.now())
                .build();
        Booking booking = new Booking(
                1L,
                itemWithId,
                LocalDateTime.now(),
                LocalDateTime.now().minusYears(1),
                user,
                BookingState.APPROVED
        );
        Mockito
                .when(bookingRepository.findAllByBooker_IdAndItem_Id(anyLong(), anyLong()))
                .thenReturn(List.of(booking));
        CommentDto commentDtoFromService = itemService.createComment(
                commentId,
                userId,
                newCommentDto
        );

        CommentDto commentDto = CommentMapper.mapToCommentDto(comment);

        Assertions.assertEquals(commentDto, commentDtoFromService, "Комментарии не совпадают при создании");
    }

    @Test
    @DisplayName("Должен обновлять вещь по id")
    void shouldUpdateItemById() {
        Mockito
                .when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(itemWithId));

        NewItemDto itemDtoOnUpdate = NewItemDto.builder()
                .name("Электротриммер")
                .build();

        ItemDto updatedItemDto = ItemDto.builder()
                .id(itemId)
                .name("Электротриммер")
                .description("Работает от сети, 1300 Вт")
                .owner(user)
                .available(true)
                .build();

        ItemDto itemDtoFromService = itemService.updateItem(itemId, userId, itemDtoOnUpdate);
        Assertions.assertEquals(updatedItemDto, itemDtoFromService, "Вещи не совпадают при обновлении");
    }

    @Test
    @DisplayName("Должен бросать исключение при удалении несуществующей вещи")
    void shouldThrowExceptionWhenRemoveNonExistItem() {
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenThrow(new NotFoundException());

        NotFoundException exceptionFromService = Assertions.assertThrows(NotFoundException.class, () -> {
            itemService.removeItem(itemId, userId);
        });

        NotFoundException exception = new NotFoundException();

        Assertions.assertEquals(
                exception.getClass(),
                exceptionFromService.getClass(),
                "Не пробросилось исключение при удалении вещи"
        );
    }
}
