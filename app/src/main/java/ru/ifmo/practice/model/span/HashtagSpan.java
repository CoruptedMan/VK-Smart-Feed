package ru.ifmo.practice.model.span;

import android.content.Context;
import android.view.View;

public class HashtagSpan extends MyClickableSpan {

    public HashtagSpan(int pStartPosition, int pEndPosition, String pString, Context pContext) {
        super(pStartPosition, pEndPosition, pString, pContext);
    }

    @Override
    public void onClick(View widget) {
        // TODO implement on click methods
        System.out.println("Click on hashtag span: " + getString());
    }

    @Override
    public String toString() {
        return "H[" + getStartPosition() + "-" + getEndPosition() + "] " + getString();
    }
}
