package com.example.kafgenerator.Services;

import com.example.kafgenerator.Entities.Roles;
import java.util.List;

public interface IRoleService {
    List<Roles> getAllRoles();

    Roles getRoleById(Long id);


}
