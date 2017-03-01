package ru.ifmo.practice.model.span;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

public class EmailSpan extends MyClickableSpan {

    public EmailSpan(int pStartPosition, int pEndPosition, String pString, Context pContext) {
        super(pStartPosition, pEndPosition, pString, pContext);
    }

    @Override
    public void onClick(View widget) {
        getTextPaint().bgColor = Color.GREEN;
        // TODO implement on click methods
        System.out.println("Click on email span: " + getString());
    }

    @Override
    public String toString() {
        return "E[" + getStartPosition() + "-" + getEndPosition() + "] " + getString();
    }
}
