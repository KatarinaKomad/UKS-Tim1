package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @CreationTimestamp
    private LocalDateTime committedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User committer;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Commit> parentCommits;

    @ManyToMany
    private List<Branch> branches;
    @OneToMany(mappedBy = "commit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;
}
