package kz.almat.almatsecurityboot.almatsecurity.controllers;

import kz.almat.almatsecurityboot.almatsecurity.entities.Users;
import kz.almat.almatsecurityboot.almatsecurity.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = {"/", "/index", "/home"})
    public String index(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "index";
    }

    @GetMapping(value = "/loginpage")
    public String loginPage(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "loginpage";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/profile")
    public String profile(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "profile";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping(value = "/adminpanel")
    public String adminPanel(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "adminpanel";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @GetMapping(value = "/moderatorpanel")
    public String moderatorPanel(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "moderatorpanel";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updateprofile")
    public String updateProfile(@RequestParam(name = "full_name") String fullName){
        Users currentUser = getUser();
        currentUser.setFullName(fullName);
        userService.updateUser(currentUser);
        String someText = "Some Text";
        return "redirect:/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/updatepassword")
    public String updatePassword(@RequestParam(name = "old_password") String oldPassword,
                                 @RequestParam(name = "new_password") String newPassword,
                                 @RequestParam(name = "confirm_new_password") String confirmNewPassword){
        Users currentUser = getUser();

        if(passwordEncoder.matches(oldPassword, currentUser.getPassword()) && newPassword.equals(confirmNewPassword)){

            currentUser.setPassword(passwordEncoder.encode(newPassword));
            userService.updateUser(currentUser);

            return "redirect:/profile?success";

        }

        return "redirect:/profile?error";
    }

    private Users getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            return (Users) authentication.getPrincipal();
        }
        return null;
    }

}