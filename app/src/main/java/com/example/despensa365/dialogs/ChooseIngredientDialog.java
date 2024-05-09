package com.example.despensa365.dialogs;

import android.app.Activity;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.adapters.IngredientAdapter;
import com.example.despensa365.objects.Ingredient;

import java.util.ArrayList;

public class ChooseIngredientDialog extends DialogFragment {
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();

    public ChooseIngredientDialog(ArrayList<Ingredient> ingredientArrayList) {
        //TODO When DB is avaible, the ingrList has to be by a call of the DB
        this.ingredientArrayList = ingredientArrayList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder window = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View windowView = inflater.inflate(R.layout.dialog_ingredients_ondb, null);

        window.setTitle(R.string.titleDialogChooseIngr);
        window.setView(windowView);

        RecyclerView rvIngrList = windowView.findViewById(R.id.rvIngrList);
        EditText tvHintIngr = windowView.findViewById(R.id.tvHintIngr);

        IngredientAdapter adapter = new IngredientAdapter(ingredientArrayList, this::openQuantityDialog);
        rvIngrList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvIngrList.setAdapter(adapter);

        window.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return window.create();
    }

    private void openQuantityDialog(Ingredient ingredient) {
        QuantityIngredientDialog dialog = new QuantityIngredientDialog(ingredient);
        dialog.show(getParentFragmentManager(), "QuantityIngredientDialog");
    }

}
