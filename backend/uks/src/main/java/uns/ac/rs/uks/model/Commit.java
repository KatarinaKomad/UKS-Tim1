package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    private String description;

    @CreationTimestamp
    private LocalDateTime committedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User committer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Commit parentCommit;

    @ManyToMany(mappedBy = "commits")
    private List<Branch> branches;

    @OneToMany(mappedBy = "commit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;
}
