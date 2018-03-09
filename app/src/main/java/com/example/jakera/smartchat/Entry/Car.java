package com.example.jakera.smartchat.Entry;

import java.util.List;

/**
 * Created by jakera on 18-3-9.
 */

public class Car {
    public double log_id;
    public List<CarInfo> result;
    public String color_result;

    public class CarInfo {
        public double score;
        public String name;
        public String year;
    }
}
