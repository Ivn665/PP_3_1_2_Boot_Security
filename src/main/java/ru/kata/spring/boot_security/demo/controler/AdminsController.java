package ru.kata.spring.boot_security.demo.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    //устанавливаем соответствие между адресом страницы и файлом представления
    @GetMapping("")
    public String showAdminPage(Model model) {
        model.addAttribute("usersList", userService.allUsers());
        return "/WEB-INF/admin";
    }

    @GetMapping("/new")
    public String showNewUserPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "WEB-INF/user_form";
    }

    @GetMapping(value = "/edit")
    public String showEditPage(@RequestParam("id") long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "WEB-INF/user_form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") @Valid User user
            , BindingResult bindingResult
            , @ModelAttribute("roles") HashSet<Role> roles) {
        if (bindingResult.hasErrors()) {
            roles.addAll(roleService.getAllRoles());
            return "/WEB-INF/user_form";
        } else {
            userService.saveUser(user);
            return "redirect:/admin";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Authentication authentication, HttpSession session) {
        User deleteUser = userService.getById(id);
        userService.deleteById(id);
        if (authentication.getName().equals(deleteUser.getUsername())) {
            session.invalidate();
            return "redirect:/";
        } else {
            return "redirect:/admin";
        }
    }
}
