package com.example.jakera.smartchat.Entry;

import java.util.List;

/**
 * Created by jakera on 18-2-23.
 */
//{"code":200,"msg":"成功!","data":[{"createdAt":"2017-07-10T08:02:25.353Z","publishedAt":"2017-07-10T12:48:49.297Z","type":"美图","url":"https://ws1.sinaimg.cn/large/610dc034ly1fhegpeu0h5j20u011iae5.jpg"},
public class BeautifulPictureEntry {
    public int code;
    public String msg;
    public List<Data> data;

    public class Data {
        public String createdAt;
        public String publishedAt;
        public String type;
        public String url;
    }

}
