package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Item {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String name;
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> assignees;

    @ManyToOne(fetch = FetchType.LAZY)
    private Milestone milestone;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Label> labels;
}
