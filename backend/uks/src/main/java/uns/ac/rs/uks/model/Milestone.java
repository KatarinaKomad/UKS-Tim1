package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private State state;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Repository repository;

    @OneToMany(mappedBy = "milestone", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Item> items;
}
