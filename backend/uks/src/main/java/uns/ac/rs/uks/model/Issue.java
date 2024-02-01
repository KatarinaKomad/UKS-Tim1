package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Issue extends Item implements Serializable {
    @ManyToMany
    private List<User> participants;
    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<IssueEvent> events;
}
