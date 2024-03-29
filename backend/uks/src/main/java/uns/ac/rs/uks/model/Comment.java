package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String code;
    @OneToOne
    private User author;
    @CreationTimestamp
    private LocalDateTime createdAt;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Commit commit;
}
