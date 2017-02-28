package ru.ifmo.practice.model.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

import ru.ifmo.practice.R;
import ru.ifmo.practice.VKSmartFeedApplication;

public class RepostNoteDialogFragment extends DialogFragment {
    private long mSourceId;
    private long mId;
    private RepostNoteDialogListener listener;
    private JSONObject mResponse;

    public void setListener(RepostNoteDialogListener pListener) {
        listener = pListener;
    }

    public interface RepostNoteDialogListener {
        void onFinishRepostNoteDialog(int resultCode, int repostCount, int likesCount);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_repost, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        final EditText messageEditText = (EditText) layout.findViewById(R.id.repost_dialog_message);
        TextView repostToWall = (TextView) layout.findViewById(R.id.repost_dialog_my_wall);
        TextView repostToGroup = (TextView) layout.findViewById(R.id.repost_dialog_group);
        mSourceId = getArguments().getLong("source_id");
        mId = getArguments().getLong("object_id");
        repostToWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VKSmartFeedApplication.isOnline()) {
                    new VKRequest("wall.repost", VKParameters.from(
                            "object", "wall" + mSourceId + "_" + mId,
                            "message", messageEditText.getText()))
                            .executeWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    try {
                                        mResponse = response.json.getJSONObject("response");
                                        int resultCode = Integer.parseInt(mResponse.get("success").toString());
                                        int repostCount = Integer.parseInt(mResponse.get("reposts_count").toString());
                                        int likesCount = Integer.parseInt(mResponse.get("likes_count").toString());
                                        dismiss();
                                        listener.onFinishRepostNoteDialog(resultCode, repostCount, likesCount);
                                    } catch (JSONException pE) {
                                        pE.printStackTrace();
                                    }
                                }
                            });
                } else {
                    dismiss();
                    listener.onFinishRepostNoteDialog(2, 0, 0);
                }
            }
        });
        repostToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(messageEditText.getText());
            }
        });
        return dialog;
    }
}