package com.octagon.root.onlinequizz.Model;

public class Ussr {
    private String username;
    private String password;

    public Ussr() {
    }

    public Ussr(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
