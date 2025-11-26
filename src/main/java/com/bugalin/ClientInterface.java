package com.bugalin;

import java.io.InputStream;

public class ClientInterface {
    private void initialize(){
        InputStream jsonFile = getClass().getResourceAsStream("/data/config.json");
    }
    public static void main(String[] args) {
        System.out.println("Test");
    }
}
