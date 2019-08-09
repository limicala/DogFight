package com.limicala.dogfight.message;

import java.util.Locale;
import java.util.ResourceBundle;

public class MessageTest {
    public static void main(String[] args) throws Exception{
        ResourceBundle resourceBundle = ResourceBundle.getBundle("com.limicala.dogfight.message.messages", Locale.US);

        String a = resourceBundle.getString("PVP");

        System.out.println(new String(resourceBundle.getString("PVP").getBytes("ISO-8859-1"), "UTF-8"));
    }
}
