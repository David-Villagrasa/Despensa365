package com.example.despensa365.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.db.DB;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.ToBuyLine;

import java.util.ArrayList;

public class ToBuyAdapter extends RecyclerView.Adapter<ToBuyAdapter.ToBuyViewHolder> {


    private ArrayList<ToBuyLine> toBuyLines;
    public boolean[] selected;
    Context customContext;

    public ToBuyAdapter(Context context, ArrayList<ToBuyLine> toBuyLines) {
        this.customContext = context;
        if (toBuyLines == null) {
            this.toBuyLines = new ArrayList<>();
        } else {
            this.toBuyLines = toBuyLines;
        }
        selected = new boolean[toBuyLines.size()];
        for (int i = 0; i < toBuyLines.size(); i++) {
            selected[i] = false;
        }
    }

    @NonNull
    @Override
    public ToBuyAdapter.ToBuyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater customInflater = (LayoutInflater) customContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = customInflater.inflate(R.layout.ingredient_search_recycler, parent, false);
        return new ToBuyAdapter.ToBuyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToBuyAdapter.ToBuyViewHolder holder, int position) {
        ToBuyLine toBuyLine = toBuyLines.get(position);
        Ingredient ingredient = DB.getIngredientById(toBuyLine.getIngredientId());

        holder.itemView.setOnClickListener(v -> {
            holder.itemView.setBackgroundColor(holder.selected ? Color.LTGRAY : 0);
            selected[position] = holder.selected;
            holder.selected = !holder.selected;
        });

        holder.ingredientName.setText(ingredient.getName());
        holder.ingredientWeight.setText(String.format("%s", toBuyLine.getQuantity()));
        holder.ingredientWeight.append(ingredient.getType().getUnit());

    }
    public void updateList(ArrayList<ToBuyLine> newList) {
        toBuyLines = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return toBuyLines.size();
    }

    public class ToBuyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView ingredientName;
        TextView ingredientWeight;
        boolean selected = false;

        public ToBuyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.tvNameIngredientRecycler);
            ingredientWeight = itemView.findViewById(R.id.tvWeightIngredientRecycler);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final int ELIMINAR = 300;
            menu.add(getAdapterPosition(), ELIMINAR, 0, "Eliminar");
        }
    }
}
