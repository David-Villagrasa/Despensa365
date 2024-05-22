package com.example.despensa365.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.MainActivity;
import com.example.despensa365.R;
import com.example.despensa365.activities.RecipeViewActivity;
import com.example.despensa365.activities.SelectRecActivity;
import com.example.despensa365.objects.Ingredient;
import com.example.despensa365.objects.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipeList;
    Context customContext;
    public boolean editable, removable;
    public int selectedPosition = -1;
    public RecipeAdapter(Context context, List<Recipe> recipeList, boolean editable, boolean removable) {
        this.customContext = context;
        this.recipeList = recipeList;
        this.editable = editable;
        this.removable = removable;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater customInflater = (LayoutInflater) customContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = customInflater.inflate(R.layout.recipe_recycler, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());

        if (customContext instanceof SelectRecActivity) {
            holder.itemView.setOnClickListener(v -> {
                selectedPosition = holder.getAdapterPosition();
                SelectRecActivity s = (SelectRecActivity) customContext;
                s.recipeSelected();
            });
        }else{
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(customContext, RecipeViewActivity.class);
                intent.putExtra("recipe", recipe);
                customContext.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }
    public void updateList(ArrayList<Recipe> newList) {
        recipeList = newList;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView recipeName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.tvRecipeTitleItem);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (editable) {
                final int EDITAR = 200;
                menu.add(getAdapterPosition(), EDITAR, 0, "Editar");
            }
            if (removable) {
                final int ELIMINAR = 300;
                menu.add(getAdapterPosition(), ELIMINAR, 1, "Eliminar");
            }
        }
    }
}
