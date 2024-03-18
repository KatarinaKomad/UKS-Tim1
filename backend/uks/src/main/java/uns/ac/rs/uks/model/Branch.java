package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Branch implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
//    @CreationTimestamp
//    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private String updatedBy;
//    @ManyToMany
//    private List<Commit> commits;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Repo repository;
}
