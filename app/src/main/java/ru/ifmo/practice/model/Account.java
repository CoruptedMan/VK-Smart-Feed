package ru.ifmo.practice.model;

public class Account {
    private long id;
    private String firstName;
    private String lastName;
    private String photoUrl;

    public Account(long pId, String pFirstName, String pLastName, String pPhotoUrl) {
        id = pId;
        firstName = pFirstName;
        lastName = pLastName;
        photoUrl = pPhotoUrl;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}
