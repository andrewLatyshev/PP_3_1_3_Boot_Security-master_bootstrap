package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
	public String showAllUsers(Model model, Principal principal) {
		model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("user", userService.showUserByName(principal.getName()));
		return "/admin/adminPage";
	}


    @GetMapping("/user")
    public String toAdminPage(Principal principal, Model model) {
        model.addAttribute("user", userService.showUserByName(principal.getName()));
        return "admin/user";
    }

    @PostMapping("/user_create" )
    public String createUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "role", required=false) String role) {
        userService.saveUser(user, role);
        return "redirect:/admin";
    }

    @GetMapping("/user_delete/{id}")
    public String removeById(@PathVariable("id") Long id) {
        userService.removeUserById(id);
        return "redirect:/admin";
    }

    @PostMapping(value = "/user-update/{id}")
    public String updateUserForm(@ModelAttribute("user") User user, @PathVariable("id")
                                 @RequestParam(value = "role", required=false) String  role) {
        userService.editUser(user, role);
        return "redirect:/admin";
    }
}
