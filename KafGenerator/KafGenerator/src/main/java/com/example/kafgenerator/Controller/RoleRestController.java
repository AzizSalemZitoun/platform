package com.example.kafgenerator.Controller;

import com.example.kafgenerator.Entities.Roles;
import com.example.kafgenerator.Services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleRestController {
    @Autowired
    IRoleService roleService;

    @GetMapping("/get/all")
    public List<Roles> getAllRoles() {
        return roleService.getAllRoles();
    }

    @GetMapping("/get/{id}")
    public Roles getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }


}
