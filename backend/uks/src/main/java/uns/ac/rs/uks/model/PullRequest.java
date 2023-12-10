package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PullRequest extends Item {

    @OneToOne
    private Branch origin;
    @OneToOne
    private Branch target;

    @OneToMany(mappedBy = "pullRequest")
    private List<Review> reviews;
}
