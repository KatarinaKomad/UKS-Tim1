package uns.ac.rs.uks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uns.ac.rs.uks.dto.request.LoginRequest;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.Member;
import uns.ac.rs.uks.model.Repository;
import uns.ac.rs.uks.model.Role;
import uns.ac.rs.uks.model.User;
import uns.ac.rs.uks.repository.MemberRepository;
import uns.ac.rs.uks.repository.RoleRepository;
import uns.ac.rs.uks.util.Constants;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class RoleServiceTest {

    @InjectMocks
    private RoleService roleService;
    @Mock
    private RoleRepository roleRepository;
    private AutoCloseable closeable;

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }


    @ParameterizedTest(name = "Finding role by name {0}")
    @ValueSource(strings = {"ROLE_ADMIN", "ROLE_USER"})
    void testGetRoleByNameSuccess(String name) {
        Role role = new Role();
        role.setName(name);

        when(roleRepository.findByName(name)).thenReturn(Optional.of(role));

        Role role1 = roleService.getRoleByName(name);
        assertNotNull(role1);
        assertEquals(name, role1.getName());
    }

    @ParameterizedTest(name = "Not finding role by name {0}")
    @ValueSource(strings = {"ROLE_COLLABORATOR", "ROLE_TEST"})
    public void noRoleByName(String name) {
        when(roleRepository.findByName(name)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> roleService.getRoleByName(name));
    }

}
