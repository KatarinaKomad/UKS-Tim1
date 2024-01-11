package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repository {
    @Id
    private UUID id;
    private String name;
    @ManyToOne
    private User owner;


    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Branch> branches;
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Milestone> milestones;
    @OneToMany(mappedBy = "repository", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Project> projects;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Label> labels;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Repository forkParent;
    @OneToMany(mappedBy = "forkParent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Repository> forkChildren;


    @ManyToMany
    private List<User> staredBy;
    @ManyToMany
    private List<User> watchers;
}
