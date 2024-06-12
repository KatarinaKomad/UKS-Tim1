package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.CommentRequest;
import uns.ac.rs.uks.dto.response.CommentDTO;
import uns.ac.rs.uks.model.Comment;
import uns.ac.rs.uks.model.Item;
import uns.ac.rs.uks.model.User;

import java.util.List;

public class CommentMapper {
    public static CommentDTO toDTO(Comment comment){
        return comment == null ? null :
                CommentDTO.builder()
                        .id(comment.getId())
                        .message(comment.getMessage())
                        .createdAt(comment.getCreatedAt())
                        .author(comment.getAuthor() != null ? UserMapper.toDTO(comment.getAuthor()) : null)
                        .itemId(comment.getItem().getId())
                        .build();
    }

    public static List<CommentDTO> toDTOs(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toDTO).toList();
    }

    public static Comment fromDTO(CommentRequest commentRequest, User user, Item item){
        return Comment.builder()
                .message(commentRequest.getMessage())
                .author(user)
                .item(item)
                .build();
    }
}
