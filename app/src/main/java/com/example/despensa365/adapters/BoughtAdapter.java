package com.example.despensa365.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.ToBuyLine;

import java.util.ArrayList;

public class BoughtAdapter extends RecyclerView.Adapter<BoughtAdapter.BoughtViewHolder>{


    private ArrayList<ToBuyLine> toBuyLines;
    Context customContext;

    public BoughtAdapter(Context context, ArrayList<ToBuyLine> toBuyLines) {
        this.customContext = context;
        if (toBuyLines == null) {
            this.toBuyLines = new ArrayList<ToBuyLine>();
        } else {
            this.toBuyLines = toBuyLines;
        }
    }

    @NonNull
    @Override
    public BoughtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater customInflater = (LayoutInflater) customContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = customInflater.inflate(R.layout.ingredient, parent, false);
        return new BoughtViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoughtViewHolder holder, int position) {
        ToBuyLine toBuyLine = toBuyLines.get(position);
        Ingredient ingredient = MainActivity.SearchIngredient(toBuyLine.getIngredientId()).get();
        holder.tvNameIngredient.setText("");
        holder.etNameIng.setText(ingredient.getName());
        holder.etNameIng.setEnabled(false);
        holder.etQuantityIngr.setText("0");

        String[] dayStrings = this.customContext.getResources().getStringArray(R.array.days_of_week_full);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.customContext, android.R.layout.simple_spinner_item, dayStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spnTypeIng.setAdapter(adapter);
        holder.spnTypeIng.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return toBuyLines.size();
    }

    public class BoughtViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView tvNameIngredient,tvTypeIng,tvTitleWeight,tvTitleExpDate;
        EditText etNameIng, etQuantityIngr,etExpDateIngr;
        Spinner spnTypeIng;

        public BoughtViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameIngredient = itemView.findViewById(R.id.tvNameIngredient);
            tvTypeIng = itemView.findViewById(R.id.tvTypeIng);
            tvTitleWeight = itemView.findViewById(R.id.tvTitleWeight);
            tvTitleExpDate = itemView.findViewById(R.id.tvTitleExpDate);
            etNameIng = itemView.findViewById(R.id.etNameIng);
            etQuantityIngr = itemView.findViewById(R.id.etWeightIngr);
            etExpDateIngr = itemView.findViewById(R.id.etExpDateIngr);
            spnTypeIng = itemView.findViewById(R.id.spinnerTypeIng);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}
