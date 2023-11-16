package uns.ac.rs.uks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@Table(name = "USERS")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    private Boolean blockedByAdmin;
    private Boolean deleted;

    @CreationTimestamp
    private LocalDate created;
    @UpdateTimestamp
    private LocalDate modified;

    private String firstName;
    private String lastName;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !deleted;
    }

    @Override
    public boolean isAccountNonLocked() { return !blockedByAdmin && !deleted;}

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() { return true;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(String firstName, String lastName) {
        this.name = firstName.concat(" ").concat(lastName);
    }

    public List<String> getRoleNames() {
        return getRoles().stream().map(Role::getName).toList();
    }

}
