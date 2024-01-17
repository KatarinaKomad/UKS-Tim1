package uns.ac.rs.uks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;


@Entity
@Data
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    private UUID id;
    // ************************************* ACCOUNT ******************************************/
    @Column(unique = true, nullable = false)
    private String email;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    private String firstName;
    private String lastName;
    @ManyToOne
    private Role role; // Admin / User
    private Boolean blockedByAdmin;
    private Boolean deleted;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ************************************* GITHUB ******************************************/
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Repo> repositories;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Repo> watching;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Repo> stared;


    @OneToMany(mappedBy = "updatedBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Branch> branches;
    @OneToMany(mappedBy = "committer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Commit> commits;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Issue> issues;
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Item> myItems;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Item> assignedItems;

    // ************************************* auth ******************************************/
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
        return Collections.singletonList(role);
    }
    @Override
    public String getPassword() {
        return password;
    }

    public String getName() {
        StringBuilder builder = new StringBuilder();
        if (firstName != null) {
            builder.append(firstName);
            if (lastName != null) {
                builder.append(" ").append(lastName);
            }
        } else if (lastName != null) {
            return lastName;
        }
        return builder.toString();
    }
}
