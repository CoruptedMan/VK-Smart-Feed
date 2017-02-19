package ru.ifmo.practice.model.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ru.ifmo.practice.R;

public class RepostNoteDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.repost_dialog_layout, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        final EditText messageEditText = (EditText) layout.findViewById(R.id.repost_dialog_message);
        TextView repostToWall = (TextView) layout.findViewById(R.id.repost_dialog_my_wall);
        TextView repostToGroup = (TextView) layout.findViewById(R.id.repost_dialog_group);
        repostToWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(messageEditText.getText());
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
