package com.example.jakera.smartchat.Entry;

import java.util.List;

/**
 * Created by jakera on 18-3-13.
 */

public class Dish {
    public long log_id;
    public int result_num;
    public List<DishInfo> result;

    public class DishInfo {
        public String calorie;
        public boolean has_calorie;
        public String name;
        public String probability;

    }
}
