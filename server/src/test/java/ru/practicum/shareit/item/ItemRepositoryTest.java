package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest(
        properties = "jdbc:h2:mem:shareit",
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("ItemRepository")
public class ItemRepositoryTest extends ItemInit {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    @DisplayName("Должен находить вещи по тексту в названии или описании")
    void shouldFindItemsByTextInNameOrDescription() {
        User userFromRep = userRepository.save(UserMapper.mapFromRequest(newUserDto));
        Item itemFromRep = itemRepository.save(ItemMapper.mapFromRequest(newItemDto, userFromRep));
        List<Item> itemsFromRepository = itemRepository.findAllByText(itemFromRep.getName());
        Assertions.assertEquals(
                List.of(itemFromRep),
                itemsFromRepository,
                "Списки вещей не совпадают при поиске по тексту в БД"
        );
    }

    @Test
    @DisplayName("Должен находить вещи по id владельца")
    void shouldFindItemsByOwnerId() {
        User userFromRep = userRepository.save(UserMapper.mapFromRequest(newUserDto));
        Item itemFromRep = itemRepository.save(ItemMapper.mapFromRequest(newItemDto, userFromRep));
        List<Item> itemsFromRepository = itemRepository.findAllByOwner_id(userFromRep.getId());
        Assertions.assertEquals(
                List.of(itemFromRep),
                itemsFromRepository,
                "Списки вещей не совпадают при поиске по id владельца в БД"
        );
    }

    @Test
    @DisplayName("Должен находить вещи по id запроса (вещи в тесте не созданы по запросу)")
    void shouldFindItemsByRequestId() {
        User userFromRep = userRepository.save(UserMapper.mapFromRequest(newUserDto));
        itemRepository.save(ItemMapper.mapFromRequest(newItemDto, userFromRep));
        List<Item> itemsFromRepository = itemRepository.findAllByRequest_Id(itemId);
        Assertions.assertEquals(
                new ArrayList<>(),
                itemsFromRepository,
                "Список вещей не пуст в результате поиска по id(неправильному) запроса"
        );
    }
}
