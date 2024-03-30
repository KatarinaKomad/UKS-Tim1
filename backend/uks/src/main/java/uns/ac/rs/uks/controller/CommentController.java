package uns.ac.rs.uks.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uns.ac.rs.uks.dto.request.CommentRequest;
import uns.ac.rs.uks.dto.response.CommentDTO;
import uns.ac.rs.uks.service.CommentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/comment")
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/getAllItemComments/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<CommentDTO> getAllItemComments(@PathVariable UUID itemId) {
        return commentService.getAllItemComments(itemId);
    }

    @PostMapping("/create/{itemId}/{authorId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommentDTO createNewComment(@PathVariable UUID itemId, @PathVariable UUID authorId, @Valid @RequestBody CommentRequest commentRequest) {
        return commentService.createNewComment(itemId, authorId, commentRequest);
    }

    @PatchMapping("/edit/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommentDTO editComment(@PathVariable Long commentId, @Valid @RequestBody CommentRequest commentRequest) {
        return commentService.editComment(commentId, commentRequest);
    }

    @DeleteMapping("/delete/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }
}
