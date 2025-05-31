package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dao.ItemServiceImpl;
import ru.practicum.shareit.item.dto.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
@DisplayName("ItemController")
public class ItemControllerTest extends ItemInit {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemServiceImpl itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("Должен получать вещи")
    void shouldGetItems() throws Exception {
        List<ItemDto> itemsDto = List.of(itemDto);

        when(itemService.getItems(anyLong()))
                .thenReturn(itemsDto);

        mvc.perform(get("/items")
                        .header(headerCurrentUser, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemsDto)));
    }

    @Test
    @DisplayName("Должен находить вещь по id")
    void shouldFindItemById() throws Exception {
        ItemLongDto itemLongDto = ItemLongDto.builder()
                .id(itemId)
                .name("Дрель")
                .description("Работает от сети, 1300 Вт")
                .owner(user)
                .available(true)
                .build();

        when(itemService.getItemById(anyLong()))
                .thenReturn(itemLongDto);

        mvc.perform(get("/items/" + itemId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemLongDto.getId()))
                .andExpect(jsonPath("$.name").value(itemLongDto.getName()))
                .andExpect(jsonPath("$.description").value(itemLongDto.getDescription()))
                .andExpect(jsonPath("$.owner").value(itemLongDto.getOwner()))
                .andExpect(jsonPath("$.available").value(itemLongDto.getAvailable()));

    }

    @Test
    @DisplayName("Должен находить вещи по тексту")
    void shouldFindItemsByText() throws Exception {
        List<ItemDto> itemsDto = List.of(itemDto);

        when(itemService.getItemsByText(anyString()))
                .thenReturn(itemsDto);

        mvc.perform(get("/items/search")
                        .param("text", itemDto.getName())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemsDto)));
    }

    @Test
    @DisplayName("Должен создавать вещь")
    void shouldCreateItem() throws Exception {
        when(itemService.createItem(anyLong(), any()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .header(headerCurrentUser, userId)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.owner").value(itemDto.getOwner()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }

    @Test
    @DisplayName("Должен создавать комментарий")
    void shouldCreateComment() throws Exception {
        long commentId = 1;
        String commentText = "Хорошая вещь";

        NewCommentDto newCommentDto = NewCommentDto.builder()
                .text(commentText)
                .build();

        CommentDto commentDto = CommentDto.builder()
                .id(commentId)
                .text(commentText)
                .authorName("Timur")
                .build();

        when(itemService.createComment(anyLong(), anyLong(), any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/" + itemId + "/comment")
                        .header(headerCurrentUser, userId)
                        .content(mapper.writeValueAsString(newCommentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()));
    }

    @Test
    @DisplayName("Должен обновлять вещь по id")
    void shouldUpdateItemById() throws Exception {
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

        when(itemService.updateItem(anyLong(), anyLong(), any()))
                .thenReturn(updatedItemDto);

        mvc.perform(patch("/items/" + itemId)
                        .header(headerCurrentUser, userId)
                        .content(mapper.writeValueAsString(itemDtoOnUpdate))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedItemDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedItemDto.getName()))
                .andExpect(jsonPath("$.description").value(updatedItemDto.getDescription()))
                .andExpect(jsonPath("$.owner").value(updatedItemDto.getOwner()))
                .andExpect(jsonPath("$.available").value(updatedItemDto.getAvailable()));
    }

    @Test
    @DisplayName("Должен удалять вещь по id")
    void shouldRemoveItemById() throws Exception {
        mvc.perform(delete("/items/" + itemId)
                        .header(headerCurrentUser, userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
    }
}
