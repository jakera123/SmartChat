package com.example.jakera.smartchat.Utils;

import android.util.Log;

import com.example.jakera.smartchat.SmartChatConstant;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;
import com.youdao.sdk.ydtranslate.TranslateErrorCode;
import com.youdao.sdk.ydtranslate.TranslateListener;
import com.youdao.sdk.ydtranslate.TranslateParameters;

/**
 * Created by jakera on 18-2-6.
 */

/**
 * 110错误是因为服务端的应用没有绑定相应的服务，所以得创建服务之后，再进行服务绑定。
 */

public class TranslateUtil {

    public static void translate(String fromLanguage, String toLanguage, String content, TranslateListener listener) {

        //查词对象初始化，请设置source参数为app对应的名称（英文字符串）
        Language langFrom = LanguageUtils.getLangByName(fromLanguage);
        //若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        //Language langFrom = LanguageUtils.getLangByName("自动");
        Language langTo = LanguageUtils.getLangByName(toLanguage);

        TranslateParameters tps = new TranslateParameters.Builder()
                .source(SmartChatConstant.APPNAME)
                .from(langFrom).to(langTo).build();

        Translator translator = Translator.getInstance(tps);


        //查询，返回两种情况，一种是成功，相关结果存储在result参数中，
        // 另外一种是失败，失败信息放在TranslateErrorCode 是一个枚举类，整个查询是异步的，为了简化操作，回调都是在主线程发生。

        translator.lookup(content, "requestId", listener);
    }

}
