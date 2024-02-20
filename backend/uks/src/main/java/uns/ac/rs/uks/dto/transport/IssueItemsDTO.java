package uns.ac.rs.uks.dto.transport;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IssueItemsDTO {
    private Repo repo;
    private User author;
    private List<User> assignees;
    private Milestone milestone;
    private List<Label> labels;
    //    private List<Comment> comments;
}
