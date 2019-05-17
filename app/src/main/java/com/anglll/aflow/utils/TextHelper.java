package com.anglll.aflow.utils;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

public class TextHelper {

    public static SpannableStringBuilder getStringBuilder(SpannableText... spannableText) {
        SpannableStringBuilder builder = new SpannableStringBuilder("");
        int textLength = 0;
        for (SpannableText text : spannableText) {
            builder.append(text.text);
            builder.setSpan(new ForegroundColorSpan(text.color), textLength, textLength + text.text.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(text.size, true), textLength, textLength + text.text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            textLength += text.text.length();
        }
        return builder;
    }


    public static class SpannableText {
        String text;
        int color;
        int size;

        public SpannableText(String text, int color, int size) {
            this.text = text;
            this.color = color;
            this.size = size;
        }
    }
}
