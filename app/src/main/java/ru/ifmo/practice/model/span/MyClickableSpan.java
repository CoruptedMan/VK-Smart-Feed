package ru.ifmo.practice.model.span;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.Comparator;

public class MyClickableSpan extends ClickableSpan implements Comparable<MyClickableSpan> {
    private int startPosition;
    private int endPosition;
    private int visibleEndPosition;
    private String string;
    private Context context;
    private TextPaint mTextPaint;
    private boolean mIsPressed;
    private final int PRESSED_BACKGROUND_COLOR = 0xFFA7C2DF;
    private final int NORMAL_TEXT_COLOR = 0xFF5477A3;
    private final int PRESSED_TEXT_COLOR = 0xFF5477A3;

    MyClickableSpan(int pStartPosition, int pEndPosition, String pString, Context pContext) {
        startPosition = pStartPosition;
        endPosition = pEndPosition;
        string = pString;
        context = pContext;
    }

    void setPressed(boolean isSelected) {
        mIsPressed = isSelected;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int pStartPosition) {
        startPosition = pStartPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public String getString() {
        return string;
    }

    public void setString(String pString) {
        string = pString;
    }

    public int getVisibleEndPosition() {
        return visibleEndPosition;
    }

    public void setVisibleEndPosition(int pVisibleEndPosition) {
        visibleEndPosition = pVisibleEndPosition;
    }

    public Context getContext() {
        return context;
    }

    TextPaint getTextPaint() {
        return mTextPaint;
    }

    @Override
    public void onClick(View widget) {
        // TODO implement on click methods
        System.out.println("Click on my clickable span: " + string);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        mTextPaint = ds;
        ds.setUnderlineText(false);
        //ds.setColor();
        ds.setColor(mIsPressed ? PRESSED_TEXT_COLOR : NORMAL_TEXT_COLOR);
        ds.bgColor = mIsPressed ? PRESSED_BACKGROUND_COLOR : 0xFFFFFFFF;
    }

    @Override
    public String toString() {
        return "[" + startPosition + "-" + endPosition + "] " + string;
    }

    @Override
    public int compareTo(@NonNull MyClickableSpan o) {
        //ascending order
        return this.getStartPosition() - o.getStartPosition();

        //descending order
        //return o.getStartPosition() - this.getStartPosition();
    }

    public static Comparator<MyClickableSpan> MyClickableSpanComparator
            = new Comparator<MyClickableSpan>() {
        public int compare(MyClickableSpan o1, MyClickableSpan o2) {
            //ascending order
            return o1.compareTo(o2);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }
    };
}
