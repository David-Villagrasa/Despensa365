package com.example.despensa365.adapters;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.objects.Ingredient;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private ArrayList<Ingredient> ingredientList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Ingredient ingredient);
    }

    public IngredientAdapter(ArrayList<Ingredient> ingredientList, OnItemClickListener listener) {
        this.ingredientList = ingredientList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_recipe_recycler, parent, false);
        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.ingredientName.setText(ingredient.getName());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(ingredient));
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public void updateList(ArrayList<Ingredient> newList) {
        ingredientList = newList;
        notifyDataSetChanged();
    }
    public static class IngredientViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView ingredientName;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.tvNameIngredientListIngredients);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final int ELIMINAR = 300;
            menu.add(getAdapterPosition(), ELIMINAR, 0, "Eliminar");
        }
    }
}
