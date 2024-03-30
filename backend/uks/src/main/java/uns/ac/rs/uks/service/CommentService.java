package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.request.CommentRequest;
import uns.ac.rs.uks.dto.response.CommentDTO;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.mapper.CommentMapper;
import uns.ac.rs.uks.model.Comment;
import uns.ac.rs.uks.model.Item;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.CommentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    public Comment getById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()->new NotFoundException("Comment not found."));
    }

    public List<CommentDTO> getAllItemComments(UUID itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        return CommentMapper.toDTOs(comments);
    }

    public CommentDTO createNewComment(UUID itemId, UUID authorId, CommentRequest commentRequest) throws NotFoundException {
        User user = userService.getById(authorId);
        Item item = itemService.getById(itemId);
        Comment comment = CommentMapper.fromDTO(commentRequest, user, item);
        comment = commentRepository.save(comment);
        return CommentMapper.toDTO(comment);
    }

    public CommentDTO editComment(Long commentId, CommentRequest commentRequest) throws NotFoundException {
        Comment comment = getById(commentId);
        comment.setMessage(commentRequest.getMessage());
        comment = commentRepository.save(comment);
        return CommentMapper.toDTO(comment);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
