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
import com.example.despensa365.objects.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    Context customContext;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.customContext = context;
        this.recipeList = recipeList;
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
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView recipeName;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.tvRecipeTitleItem);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            final int EDITAR = 200;
            final int ELIMINAR = 300;
            menu.add(getAdapterPosition(),EDITAR,0,"Editar");
            menu.add(getAdapterPosition(),ELIMINAR,1,"Eliminar");
        }
    }
}
