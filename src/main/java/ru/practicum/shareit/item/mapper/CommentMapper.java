package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentView;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {
    public static CommentView mapToCommentView(Comment comment) {
        return CommentView.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static List<CommentView> mapToCommentView(Iterable<Comment> comments) {
        List<CommentView> commentsResult = new ArrayList<>();

        for (Comment comment : comments) {
            commentsResult.add(mapToCommentView(comment));
        }

        return commentsResult;
    }

    public static Comment mapToNewComment(Comment comment, User author, Item item) {
        return new Comment(
                comment.getText(),
                author,
                item
        );
    }
}
