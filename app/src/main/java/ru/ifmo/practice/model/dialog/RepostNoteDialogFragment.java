package ru.ifmo.practice.model.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    private boolean mIsUserReposted;
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
        Button repostToWall = (Button) layout.findViewById(R.id.repost_dialog_my_wall);
        Button repostToGroup = (Button) layout.findViewById(R.id.repost_dialog_group);
        mSourceId = getArguments().getLong("source_id");
        mId = getArguments().getLong("object_id");
        mIsUserReposted = getArguments().getBoolean("is_reposted");
        System.out.println("from RNDF: " + mIsUserReposted);
        if (mIsUserReposted) {
            repostToWall.setEnabled(false);
        } else {
            repostToWall.setEnabled(true);
        }

        repostToWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VKSmartFeedApplication.isOnline()) {
                    //if (!mIsUserReposted) {
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
                    /*} else {
                        final long[] repostedNoteId = {-1};
                        new VKRequest("wall.get", VKParameters.from("owner_id", LoginActivity.mAccount.getId(),
                                "count", 0, "extended", 0))
                                .executeSyncWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                try {
                                    mResponse = response.json.getJSONObject("response");
                                    for (int i = 0; i < Integer.parseInt(mResponse.get("count").toString()); i++) {
                                        if (mResponse
                                                .getJSONArray("items")
                                                .getJSONObject(i)
                                                .optJSONArray("copy_history") != null) {
                                            if (Long.parseLong(mResponse
                                                    .getJSONArray("items")
                                                    .getJSONObject(i)
                                                    .getJSONArray("copy_history")
                                                    .getJSONObject(0)
                                                    .get("id")
                                                    .toString()) == mId) {
                                                repostedNoteId[0] = Long.parseLong(mResponse
                                                        .getJSONArray("items")
                                                        .getJSONObject(i)
                                                        .get("id")
                                                        .toString());
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        if (repostedNoteId[0] > -1) {
                            new VKRequest("wall.delete", VKParameters.from("owner_id", LoginActivity.mAccount.getId(),
                                    "post_id", repostedNoteId[0]))
                                    .executeSyncWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    dismiss();
                                    listener.onFinishRepostNoteDialog(2, 0, 0);
                                }
                            });
                        }
                    }*/
                } else {
                    dismiss();
                    listener.onFinishRepostNoteDialog(3, 0, 0);
                }
            }
        });

        /*repostToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                String[] items = new String[] {"One", "Two", "Three"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                        R.layout.dialog_repost, items);
                new AlertDialog.Builder(getActivity().getApplicationContext())
                        .setTitle("the prompt")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO: user specific action
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });*/
        repostToGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(messageEditText.getText());
            }
        });
        return dialog;
    }
}