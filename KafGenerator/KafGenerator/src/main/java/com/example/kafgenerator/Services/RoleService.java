package com.example.kafgenerator.Services;
import com.example.kafgenerator.Entities.Roles;
import com.example.kafgenerator.Entities.User;
import com.example.kafgenerator.Repositories.RoleRepository;
import com.example.kafgenerator.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService implements IRoleService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
   @Override
   public List<Roles> getAllRoles() {
        return roleRepository.findAll().stream().collect(Collectors.toList());
   }

    @Override
    public Roles getRoleById(Long id) {
     return roleRepository.findById(id).orElse(null);
    }







}
