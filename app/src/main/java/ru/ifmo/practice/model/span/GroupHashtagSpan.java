package ru.ifmo.practice.model.span;

import android.content.Context;
import android.view.View;

public class GroupHashtagSpan extends MyClickableSpan {

    public GroupHashtagSpan(int pStartPosition, int pEndPosition, String pString, Context pContext) {
        super(pStartPosition, pEndPosition, pString, pContext);
    }

    @Override
    public void onClick(View widget) {
        // TODO implement on click methods
        System.out.println("Click on group hashtag span: " + getString());
    }

    @Override
    public String toString() {
        return "G[" + getStartPosition() + "-" + getEndPosition() + "] " + getString();
    }
}
