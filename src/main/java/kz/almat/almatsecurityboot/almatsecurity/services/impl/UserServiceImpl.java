package kz.almat.almatsecurityboot.almatsecurity.services.impl;

import kz.almat.almatsecurityboot.almatsecurity.entities.Users;
import kz.almat.almatsecurityboot.almatsecurity.repositories.UserRepository;
import kz.almat.almatsecurityboot.almatsecurity.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(s);
        if(user!=null) return user;
        else throw new UsernameNotFoundException("User Not Found");
    }

    @Override
    public Users updateUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public Users getUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users addUser(Users user) {
        return userRepository.save(user);
    }
}
