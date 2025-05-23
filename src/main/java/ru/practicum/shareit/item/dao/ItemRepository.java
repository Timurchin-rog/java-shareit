package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(%?1%) " +
            "OR UPPER(i.description) LIKE UPPER(%?1%)) " +
            "AND i.available = true")
    List<Item> findAllByText(String text);

    List<Item> findAllByOwner_id(long ownerId);
}
