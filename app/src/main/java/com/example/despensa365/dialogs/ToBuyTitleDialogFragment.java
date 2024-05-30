package com.example.despensa365.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import com.example.despensa365.R;

public class ToBuyTitleDialogFragment extends DialogFragment {

    public interface ToBuyTitleDialogListener {
        void onDialogPositiveClick(String title);
        void onDialogNegativeClick(ToBuyTitleDialogFragment dialog);
    }

    private ToBuyTitleDialogListener listener;

    public void setListener(ToBuyTitleDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.toBuyDialogTitle);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_to_buy_title, null);
        final EditText input = view.findViewById(R.id.etToBuyTitle);

        builder.setView(view)
                .setPositiveButton("OK", (dialog, id) -> {
                    if (listener != null) {
                        String title = input.getText().toString();
                        listener.onDialogPositiveClick(title);
                    }
                })
                .setNegativeButton(R.string.back, (dialog, id) -> {
                    if (listener != null) {
                        listener.onDialogNegativeClick(ToBuyTitleDialogFragment.this);
                    }
                });
        return builder.create();
    }
}
