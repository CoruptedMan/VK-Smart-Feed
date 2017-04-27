package ru.ifmo.practice.model.span;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;

public class LinkSpan extends MyClickableSpan {
    private String shortString;
    private String parsedUrl;

    public LinkSpan(int pStartPosition, int pEndPosition, String pString, Context pContext) {
        super(pStartPosition, pEndPosition, pString, pContext);
        if (pString.length() > 50) {
            shortString = pString.substring(0, 50) + "...";
        } else {
            shortString = "";
        }
        parsedUrl = getString();
    }

    public String getShortString() {
        return shortString;
    }

    @Override
    public void onClick(View widget) {
        System.out.println(this.toString());
        VKRequest request = new VKRequest("utils.checkLink", VKParameters.from("url", getString()));
        System.out.println(request.toString());
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    parsedUrl = response.json.getJSONObject("response").get("link").toString();
                } catch (JSONException pE) {
                    pE.printStackTrace();
                }
            }
            @Override
            public void onError(VKError error) {
                Log.e("checkLink", error.toString());
            }
        });
        System.out.println(getString() + " | " + parsedUrl);
        /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(parsedUrl));
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(browserIntent);*/
    }

    @Override
    public String toString() {
        return "L[" + getStartPosition() + "-" + getEndPosition() + "] " + getString();
    }
}
