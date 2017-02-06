package ru.ifmo.practice.model;

public class User {

    private int id;
    private String firstName;
    private String lastName;

    public int getId() {
        return id;
    }

    public void setId(int pId) {
        id = pId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String pFirstName) {
        firstName = pFirstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String pLastName) {
        lastName = pLastName;
    }

    public User(int pId, String pFirstName, String pLastName) {
        id = pId;
        firstName = pFirstName;
        lastName = pLastName;
    }
}
