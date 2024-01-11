package uns.ac.rs.uks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.exception.NotFoundException;
import uns.ac.rs.uks.model.Role;
import uns.ac.rs.uks.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;


    public Role getRoleByName(String name) {
        return roleRepository.findByName(name).orElseThrow(() -> new NotFoundException("Role not found"));
    }
}
