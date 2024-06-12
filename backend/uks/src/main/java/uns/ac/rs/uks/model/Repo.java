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
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repo implements Serializable {

    @Id
    private UUID id;
    private String name;
    private String description;
    @ManyToOne
    private User owner;

    private Boolean isPublic;

    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Branch> branches;
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Milestone> milestones;
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Project> projects;
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Label> labels;
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Issue> issues;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Branch defaultBranch;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Repo forkParent;
    @OneToMany(mappedBy = "forkParent", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Repo> forkChildren;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> staredBy;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> watchers;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private String cloneUri;

}
