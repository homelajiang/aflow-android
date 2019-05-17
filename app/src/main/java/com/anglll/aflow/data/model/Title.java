package com.anglll.aflow.data.model;

import android.support.annotation.DrawableRes;

/**
 * Created by yuan on 2017/12/6 0006.
 */

public class Title {
    String content;
    @DrawableRes
    int imgRes;

    public Title(String content, int imgRes) {
        this.content = content;
        this.imgRes = imgRes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

}
