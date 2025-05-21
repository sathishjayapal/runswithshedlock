package me.sathish.runswithshedlock.runner;

import jakarta.validation.constraints.Size;


public class RunnerDTO {

    private Long id;

    @Size(max = 255)
    private String username;

    @Size(max = 255)
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

}
