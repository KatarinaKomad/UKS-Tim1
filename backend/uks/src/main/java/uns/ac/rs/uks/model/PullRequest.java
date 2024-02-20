package uns.ac.rs.uks.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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

    private String origin;
    private String target;

    @OneToMany  // (mappedBy = "pullRequest")
    private List<Review> reviews;

    @OneToMany(mappedBy = "pullRequest", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PullRequestEvent> events;
}
