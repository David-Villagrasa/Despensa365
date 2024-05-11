package com.example.despensa365.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.despensa365.R;
import com.example.despensa365.activities.SelectIngrActivity;
import com.example.despensa365.objects.Ingredient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QuantityDateIngredientDialog extends DialogFragment {
    EditText etQuantity,etDate;
    Ingredient ingredient;

    private SelectIngrActivity.Callback callback;

    public QuantityDateIngredientDialog(Ingredient ingredient, SelectIngrActivity.Callback callback) {
        this.ingredient = ingredient;
        this.callback = callback;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //datos = (Datos) getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder window = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View windowView = inflater.inflate(R.layout.dialog_how_much_and_date, null);

        window.setTitle(R.string.titleDialogChooseIngr);
        window.setView(windowView);

        etQuantity = windowView.findViewById(R.id.etHowMuchSecondary);
        etDate = windowView.findViewById(R.id.etDateSecondary);

        window.setNegativeButton(R.string.back, (dialog, which) -> dialog.cancel());
        window.setPositiveButton(R.string.next, (dialog, which) -> {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            Date d =new Date();
            try {
                d = df.parse(etDate.getText().toString());
            } catch (ParseException e) {}
            callback.dialogOK(this.ingredient, Double.parseDouble(etQuantity.getText().toString()), d);
        });

        return window.create();
    }/*

        public interface Datos {
            public void pasarDatos(String nombre, int edad, int codigo);
        }*/

}
