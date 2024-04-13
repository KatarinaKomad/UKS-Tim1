package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PullRequest extends Item implements Serializable {

    @OneToOne
    private Branch origin;

    @OneToOne
    private Branch target;

    @OneToMany  // (mappedBy = "pullRequest")
    private List<Review> reviews;

    @OneToMany(mappedBy = "pullRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PullRequestEvent> events;
}
