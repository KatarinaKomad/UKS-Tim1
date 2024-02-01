package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Repo forkParent;
    @OneToMany(mappedBy = "forkParent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Repo> forkChildren;


    @ManyToMany
    private List<User> staredBy;
    @ManyToMany
    private List<User> watchers;
}
