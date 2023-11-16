package uns.ac.rs.uks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uns.ac.rs.uks.dto.RoleEnum;
import uns.ac.rs.uks.model.Role;
import uns.ac.rs.uks.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    public Role getRole(RoleEnum role){
        logger.info("Try to get role by name");
        return roleRepository.findByName(role.getName());
    }

    public List<Role> getRoles(List<RoleEnum> roles){
        logger.info("Getting all roles");
        return roles.stream().map(this::getRole).toList();
    }
}
