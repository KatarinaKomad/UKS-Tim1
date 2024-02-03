package uns.ac.rs.uks.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Label implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color; // hex
    private String description;
    @ManyToOne
    private Repo repository;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "labels")
    private List<Item> items;
}
