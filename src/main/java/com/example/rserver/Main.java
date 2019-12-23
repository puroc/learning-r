package com.example.rserver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "2019-02-02 22:59:59";
        Date date = sdf.parse(str);
        System.out.println(sdf.format(date));
        System.out.println(date.getTime());

        Date date2 = new Date(1549119599000L);
        System.out.println(sdf.format(date2));
    }
}
