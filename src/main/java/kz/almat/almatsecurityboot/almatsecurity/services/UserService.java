package kz.almat.almatsecurityboot.almatsecurity.services;

import kz.almat.almatsecurityboot.almatsecurity.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Users updateUser(Users user);
    Users getUser(String email);
    Users addUser(Users user);

}
