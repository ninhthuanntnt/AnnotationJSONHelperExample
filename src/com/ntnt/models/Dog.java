package com.ntnt.models;

import com.ntnt.annotations.JsonProperty;
import com.ntnt.annotations.JsonSerializable;

import java.io.Serializable;

@JsonSerializable
public class Dog implements Serializable {
    private String name;

    @JsonProperty(name = "color")
    private String color;
    private boolean alive;
    private int age;
    public Dog(){

    }

    public Dog(String name, String color, boolean alive, int age) {
        this.name = name;
        this.color = color;
        this.alive = alive;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
