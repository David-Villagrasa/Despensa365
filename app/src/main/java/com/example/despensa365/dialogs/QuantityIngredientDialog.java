package com.example.despensa365.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.activities.SelectIngrActivity;
import com.example.despensa365.objects.Ingredient;

public class QuantityIngredientDialog extends DialogFragment {
    EditText etQuantity;
    Ingredient ingredient;

    private SelectIngrActivity.Callback callback;

    public QuantityIngredientDialog(Ingredient ingredient, SelectIngrActivity.Callback callback) {
        this.ingredient = ingredient;
        this.callback = callback;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder window = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View windowView = inflater.inflate(R.layout.dialog_how_much, null);

        window.setTitle(R.string.titleDialogChooseIngr);
        window.setView(windowView);

        etQuantity = windowView.findViewById(R.id.etHowMuch);

        window.setNegativeButton(R.string.back, (dialog, which) -> dialog.cancel());
        window.setPositiveButton(R.string.next, (dialog, which) -> {
            if (etQuantity.getText().toString().isEmpty()) {
                etQuantity.setError(getString(R.string.mustFill));
            }else{
                callback.dialogOK(this.ingredient, Double.parseDouble(etQuantity.getText().toString()), null);
            }
        });

        return window.create();
    }/*

        public interface Datos {
            public void pasarDatos(String nombre, int edad, int codigo);
        }*/

}
