package com.example.despensa365.adapters;

import android.content.Context;
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
import com.example.despensa365.objects.RecipeLine;

import java.util.ArrayList;

public class IngredientsRecipeAdapter extends RecyclerView.Adapter<IngredientsRecipeAdapter.IngredientsRecipeViewHolder> {

    private ArrayList<RecipeLine> recipeLineList;
    Context customContext;

    public IngredientsRecipeAdapter(Context context, ArrayList<RecipeLine> recipeLineList) {
        this.customContext = context;
        if (recipeLineList == null) {
            this.recipeLineList = new ArrayList<>();
        } else {
            this.recipeLineList = recipeLineList;
        }
    }

    @NonNull
    @Override
    public IngredientsRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater customInflater = (LayoutInflater) customContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = customInflater.inflate(R.layout.ingredient_search_recycler, parent, false);
        return new IngredientsRecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsRecipeViewHolder holder, int position) {
        RecipeLine recipeLine = recipeLineList.get(position);
        Ingredient ingredient = DB.getIngredientById(recipeLine.getIdIngredient());
        holder.ingredientName.setText(ingredient.getName());
        holder.ingredientWeight.setText(recipeLine.getQuantity() + ingredient.getType().getUnit());
    }

    @Override
    public int getItemCount() {
        return recipeLineList.size();
    }

    public class IngredientsRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView ingredientName;
        TextView ingredientWeight;

        public IngredientsRecipeViewHolder(@NonNull View itemView) {
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
