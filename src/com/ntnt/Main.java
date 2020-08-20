package com.ntnt;

import com.ntnt.helpers.JSONHelper;
import com.ntnt.models.Dog;

public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog("Ki", "Yellow", false, 20);
        System.out.println(JSONHelper.stringify(dog));
        Dog dog1 = (Dog) JSONHelper.parse(JSONHelper.stringify(dog), Dog.class);
    }
}
