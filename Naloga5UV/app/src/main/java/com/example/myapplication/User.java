package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;



// Entity: User
@Entity(tableName = "users")
public class User {
    @PrimaryKey
    @NonNull
    private String email;

    @NonNull
    private String password;

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

