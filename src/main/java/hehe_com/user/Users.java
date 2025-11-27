package hehe_com.user;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String username;
    private String email;
    private String password;

    private ROLES role;



    private final Date createdAt;
    private Date updatedAt;

    public Users(String username, String email, ROLES role) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Users() {
        this.id = UUID.randomUUID();
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public void setUsername(String username) {
        this.username = username;
        this.updatedAt = new Date();
    }

    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = new Date();
    }

    public void setRole(ROLES role) {
        this.role = role;
        this.updatedAt = new Date();
    }

    public void setPassword(String password) {
        this.password = password;
        this.updatedAt = new Date();
    }


    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public ROLES getRole() {
        return role;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
