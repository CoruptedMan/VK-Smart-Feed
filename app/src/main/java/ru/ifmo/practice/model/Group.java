package ru.ifmo.practice.model;

public class Group {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int pId) {
        id = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }
    public Group(int pId, String pName) {
        id = pId;
        name = pName;
    }
}
