package com.example.jakera.smartchat.Entry;

import java.util.List;

/**
 * Created by jakera on 18-3-12.
 */

// {"log_id": 5501585968413412039, "result": [{"score": "0.91606", "name": "美国短毛猫"}, {"score": "0.0342665", "name": "美国硬毛猫"}, {"score": "0.00994411", "name": "美国短尾猫"}, {"score": "0.0085082", "name": "埃及猫"}, {"score": "0.00656378", "name": "短毛家猫"}, {"score": "0.00562214", "name": "豹猫"}]}
public class AnimalOrPlant {
    public long log_id;
    public List<AnimalInfo> result;

    public class AnimalInfo {
        public String score;
        public String name;
    }

}
