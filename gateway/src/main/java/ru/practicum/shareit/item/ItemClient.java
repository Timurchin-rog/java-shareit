package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getItems(long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> getItemById(long itemId) {
        return get("/" + itemId);
    }

    public ResponseEntity<Object> getItemsByText(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", parameters);
    }

    public ResponseEntity<Object> createItem(long ownerId, NewItemDto item) {
        return post("", ownerId, item);
    }

    public ResponseEntity<Object> createComment(long itemId, long authorId, NewCommentDto comment) {
        return post("/" + itemId + "/comment", authorId, comment);
    }

    public ResponseEntity<Object> updateItem(long itemId, long ownerId, NewItemDto item) {
        return patch("/" + itemId, ownerId, item);
    }

    public ResponseEntity<Object> removeItem(long itemId, long ownerId) {
        return delete("/" + itemId, ownerId);
    }
}
