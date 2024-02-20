package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullRequestEvent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private PullRequest pullRequest;
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    private String newValue;
    @Enumerated(EnumType.STRING)
    private IssueEventType type;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
