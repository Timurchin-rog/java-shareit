package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {
    public static CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentDto> mapToCommentDto(Iterable<Comment> comments) {
        List<CommentDto> commentsResult = new ArrayList<>();

        for (Comment comment : comments) {
            commentsResult.add(mapToCommentDto(comment));
        }

        return commentsResult;
    }

    public static Comment mapToNewComment(NewCommentDto comment, User author, Item item) {
        return new Comment(
                comment.getText(),
                author,
                item,
                comment.getCreated()
        );
    }
}
