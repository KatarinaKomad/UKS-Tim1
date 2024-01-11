package uns.ac.rs.uks.repository;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.ActiveProfiles;
import uns.ac.rs.uks.model.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @ParameterizedTest(name = "Finding role by name {0}")
    @ValueSource(strings = {"ROLE_ADMIN", "ROLE_USER"})
    public void roleWithNameExists(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        assertTrue(optionalRole.isPresent());
        Role role = optionalRole.get();
        assertEquals(name, role.getName());
    }

    @ParameterizedTest(name = "Not finding role by name {0}")
    @ValueSource(strings = {"ROLE_COLLABORATOR", "ROLE_TEST"})
    public void noRoleWithName(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        assertTrue(optionalRole.isEmpty());
    }
}
