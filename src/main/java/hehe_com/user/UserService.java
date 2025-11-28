package hehe_com.user;

import hehe_com.utils.PasswordUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        this.repository = repository;
    }
    
    public Users createUser(String username, String email, String password) {
        
        if (repository.exists(username)) {
            throw new RuntimeException("Username sudah digunakan!");
        }
        
        Users user = new Users(username, email, ROLES.USER);
        
        user.setPassword( PasswordUtil.hashPassword(password));
        
        repository.save(user);
        repository.saveLogin(user);
        
        return user;
    }
    
    public Users Login(String username, String password) {
        Users user = repository.findByUsername(username);
        if (user == null) return null;
        if(!PasswordUtil.checkPassword(password,user.getPassword())) return null;
        repository.saveLogin(user);
        return user;
    }
    
    public void logout(){
        repository.deleteUserLogin();
    }
    
    public Users getUserLogin(){
        return repository.loadUserLogin();
    }
}
