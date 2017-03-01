package ru.ifmo.practice.model.span;

import android.content.Context;
import android.view.View;

public class ReferenceSpan extends MyClickableSpan {
    private String id;
    private String name;

    public ReferenceSpan(int pStartPosition, int pEndPosition, String pString, Context pContext) {
        super(pStartPosition, pEndPosition, pString, pContext);
        int delimiterIndex = pString.indexOf('|');
        id = pString.substring(1, delimiterIndex);
        name = pString.substring(delimiterIndex + 1, pString.length() - 1);
    }

    public String getId() {
        return id;
    }

    public void setId(String pId) {
        id = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    @Override
    public void onClick(View widget) {
        // TODO implement on click methods
        System.out.println("Click on reference span: " + getString());
    }

    @Override
    public String toString() {
        return "R[" + getStartPosition() + "-" + getEndPosition() + "] " + id + ":" + name;
    }
}
