package com.example.despensa365.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.db.DB;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.PantryLine;
import com.example.despensa365.objects.RecipeLine;

import java.util.ArrayList;

public class IngredientsPantryAdapter extends RecyclerView.Adapter<IngredientsPantryAdapter.IngredientsPantryViewHolder> {

    public ArrayList<PantryLine> pantryLinesList;
    Context customContext;

    public IngredientsPantryAdapter(Context context, ArrayList<PantryLine> pantryLinesList) {
        this.customContext = context;
        if (pantryLinesList == null) {
            this.pantryLinesList = new ArrayList<PantryLine>();
        } else {
            this.pantryLinesList = pantryLinesList;
        }
    }

    @NonNull
    @Override
    public IngredientsPantryAdapter.IngredientsPantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater customInflater = (LayoutInflater) customContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = customInflater.inflate(R.layout.ingredient_pantry_recycler, parent, false);
        return new IngredientsPantryAdapter.IngredientsPantryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsPantryAdapter.IngredientsPantryViewHolder holder, int position) {
        PantryLine pantryLine = pantryLinesList.get(position);
        Ingredient ingredient = DB.getIngredientById(pantryLine.getIngredientId());
        holder.ingredientName.setText(ingredient.getName());
        holder.ingredientQuantity.setText(String.format("%s", pantryLine.getIngredientQuantity()));
        holder.ingredientQuantity.append(ingredient.getType().getUnit());
        holder.ingredientDate.setText(String.format("%s", pantryLine.getExpirationDate()));
    }

    @Override
    public int getItemCount() {
        return pantryLinesList.size();
    }

    public class IngredientsPantryViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView ingredientName;
        TextView ingredientQuantity;
        TextView ingredientDate;

        public IngredientsPantryViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.tvNameIngrPantry);
            ingredientQuantity = itemView.findViewById(R.id.tvQuantityIngrPantry);
            ingredientDate = itemView.findViewById(R.id.tvDateIngrPantry);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final int ELIMINAR = 300;
            menu.add(getAdapterPosition(), ELIMINAR, 0, "Eliminar");
        }
    }
}
