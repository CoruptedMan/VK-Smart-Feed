package ru.ifmo.practice.util;

import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;

public class TextViewLinksVisor {
    private final static String LOG = TextViewLinksVisor.class.getSimpleName();

    public static class CustomerTextClick extends ClickableSpan {
        String mUrl;

        public CustomerTextClick(String url) {
            mUrl = url;
        }

        @Override
        public void onClick(View widget) {
            //Тут можно как-то обработать нажатие на ссылку
            //Сейчас же мы просто открываем браузер с ней
            Log.i(LOG, "url clicked: " + this.mUrl);

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mUrl));
            widget.getContext().startActivity(i);
        }
    }

    public static SpannableStringBuilder reformatText(CharSequence text) {
        int end = text.length();
        Spannable sp = (Spannable) text;
        URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
        SpannableStringBuilder style = new SpannableStringBuilder(text);
        for (URLSpan url : urls)
        {
            style.removeSpan(url);
            TextViewLinksVisor.CustomerTextClick click = new TextViewLinksVisor.CustomerTextClick(url.getURL());
            style.setSpan(click, sp.getSpanStart(url), sp.getSpanEnd(url),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return style;
    }
}