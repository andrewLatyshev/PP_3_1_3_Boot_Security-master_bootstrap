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


    @GetMapping(path = "user/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.showUser(id));
        return "user";
    }

//    @GetMapping("/user-create")
//    public String createUserForm(Model model) {
//        model.addAttribute("user", new User());
//        return "admin/user-create";
//    }

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

    @PostMapping("/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model, @ModelAttribute("user") User user,
                                 @RequestParam(value = "role", required=false) String  role) {
        model.addAttribute("user", userService.showUser(id));
        model.addAttribute("role", roleService.getAll());
        userService.editUser(id, user, role);
        return "redirect:/admin";
    }

//    @PatchMapping(path = "/user-update")
//    public String updateUser(Long id, @ModelAttribute("user") User user,
//                             @RequestParam(name = "role", required=false) String  role) {
//        userService.editUser(id, user, role);
//        return "redirect:/admin";
//    }
}
