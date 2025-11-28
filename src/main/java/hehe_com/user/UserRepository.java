package hehe_com.user;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

public class UserRepository {
    
    private final Path USER_DIR;
    
    public UserRepository(Path USER_DIR) {
        this.USER_DIR = USER_DIR;
    }
    
    public void save(Users user) {
        try {
            if (!Files.exists(USER_DIR)) {
                Files.createDirectories(USER_DIR);
            }
            
            Path file = USER_DIR.resolve(user.getUsername() + ".usr");
            
            try (
                    OutputStream out = Files.newOutputStream(file);
                    ObjectOutputStream oos = new ObjectOutputStream(out)
            ) {
                oos.writeObject(user);
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void saveLogin(Users user) {
        try {
            Files.createDirectories(USER_DIR);
            
            deleteUserLogin();
            
            Path file = USER_DIR.resolve(".curr-login.usr");
            
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(file))) {
                oos.writeObject(user);
            }
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Users loadUserLogin() {
        Path file = USER_DIR.resolve(".curr-login.usr");
        
        if (!Files.exists(file)) return null;
        
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(file))) {
            return (Users) ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    
    public void deleteUserLogin() {
        Path file = USER_DIR.resolve(".curr-login.usr");
        
        try {
            Files.deleteIfExists(file);
        } catch (IOException ignore) {}
    }
    
    
    public Users findByUsername(String username) {
        Path file = USER_DIR.resolve(username + ".usr");
        
        if (!Files.exists(file)) {
            return null;
        }
        
        try (
                InputStream in = Files.newInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(in)
        ) {
            return (Users) ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public boolean exists(String username) {
        return Files.exists(USER_DIR.resolve(username + ".usr"));
    }
    
    public void delete(String username) {
        try {
            Files.deleteIfExists(USER_DIR.resolve(username + ".usr"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

