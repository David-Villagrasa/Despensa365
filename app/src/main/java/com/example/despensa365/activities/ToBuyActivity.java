package com.example.despensa365.activities;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.despensa365.R;
import com.example.despensa365.adapters.BoughtAdapter;
import com.example.despensa365.adapters.ToBuyAdapter;
import com.example.despensa365.db.DB;
import com.example.despensa365.objects.PantryLine;
import com.example.despensa365.objects.ToBuy;
import com.example.despensa365.objects.ToBuyLine;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ToBuyActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> customLauncher;
    private ToBuyAdapter toBuyAdapter;
    private BoughtAdapter boughtAdapter;
    private RecyclerView rvToBuy;
    private FloatingActionButton toBuyIngAdd;
    private TextView tvTitleToBuy, tvHint;
    private Button btnBackToBuy, btnDone, btnAddNeededIngr;
    private ToBuy currentToBuy;
    private boolean isBought = false;
    private int posItem;
    private final String TAG = "ToBuyActivity";
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_buy_list);

        rvToBuy = findViewById(R.id.rvToBuy);
        toBuyIngAdd = findViewById(R.id.toBuyIngAdd);
        tvTitleToBuy = findViewById(R.id.tvTitleToBuy);
        btnBackToBuy = findViewById(R.id.btnBackToBuy);
        btnDone = findViewById(R.id.btnDone);
        btnAddNeededIngr = findViewById(R.id.btnAddNeededIngr);
        tvHint = findViewById(R.id.tvHintToBuy);

        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
            Intent data = resultado.getData();
            if (data != null) {
                String idIngr = data.getStringExtra("ingredient");
                double quantity = data.getDoubleExtra("quantity", -1);
                ToBuyLine newLine = new ToBuyLine("0", currentToBuy.getId(), idIngr, quantity);
                DB.addToBuyLine(DB.getCurrentUser(), newLine,(bool)->{
                    DB.reloadToBuyLines(DB.getCurrentUser(),currentToBuy.getId(),()->{
                        toBuyAdapter.updateList(DB.toBuyLinesArrayList);
                        toBuyAdapter.notifyDataSetChanged();
                    });
                });
            }
        });
        DB.getToBuy(DB.getCurrentUser(),(toBuy)->{
            currentToBuy=toBuy;
            DB.reloadToBuyLines(DB.currentUser,currentToBuy.getId(), this::setupRecyclerView);
            tvTitleToBuy.append(" " + currentToBuy.getTitle());
            setupListeners();
        });
    }


    private void setupListeners() {
        btnBackToBuy.setOnClickListener(v -> {
            if (isBought) {
                isBought = false;
                btnAddNeededIngr.setVisibility(View.VISIBLE);
                btnDone.setText(R.string.doneToBuy);
                setupRecyclerView();
                tvHint.setText("");
            } else {
                finish();
            }
        });

        btnDone.setOnClickListener(v -> {
            if (isBought) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                df.setLenient(false);

                ArrayList<ToBuyLine> linesToRemove = new ArrayList<>();

                for (int i = 0; i < boughtAdapter.getItemCount(); i++) {
                    BoughtAdapter.BoughtViewHolder holder = (BoughtAdapter.BoughtViewHolder) rvToBuy.findViewHolderForAdapterPosition(i);
                    if (holder != null) {
                        String quantityStr = holder.etQuantityIngr.getText().toString();
                        String expDateStr = holder.etExpDateIngr.getText().toString();

                        if (!quantityStr.isEmpty() && !expDateStr.isEmpty()) {
                            try {
                                double quantity = Double.parseDouble(quantityStr);
                                Date expirationDate = df.parse(expDateStr);

                                ToBuyLine toBuyLine = boughtAdapter.getToBuyLine(i);
                                PantryLine pantryLine = new PantryLine();
                                pantryLine.setIngredientId(toBuyLine.getIngredientId());
                                pantryLine.setIngredientQuantity(quantity);
                                pantryLine.setExpirationDate(expirationDate);

                                DB.getPantryId(DB.currentUser, pantryId -> {
                                    pantryLine.setPantryId(pantryId);

                                    // Add to pantry
                                    DB.addPantryLine(DB.getCurrentUser(), pantryLine, success -> {
                                        if (!success) {
                                            Log.w(TAG, "Failed to add PantryLine for ingredient: " + toBuyLine.getIngredientId());
                                        } else {
                                            // Add to remove list
                                            linesToRemove.add(toBuyLine);

                                            // Remove the validated ToBuyLine
                                            DB.deleteToBuyLine(DB.currentUser, toBuyLine.getToBuyId(), toBuyLine.getId(), deleteSuccess -> {
                                                if (deleteSuccess) {
                                                    // Update the list after removal
                                                    runOnUiThread(() -> {
                                                        DB.toBuyLinesArrayList.remove(toBuyLine);
                                                        boughtAdapter.updateList(DB.toBuyLinesArrayList);
                                                    });
                                                    btnBackToBuy.performClick();
                                                } else {
                                                    Log.w(TAG, "Failed to delete ToBuyLine with ID: " + toBuyLine.getId());
                                                }
                                            });
                                        }
                                    });
                                });
                            } catch (NumberFormatException | ParseException e) {
                                Toast.makeText(this, R.string.invalidData, Toast.LENGTH_SHORT).show();
                            } catch (java.text.ParseException e) {
                                Toast.makeText(this, R.string.invalidData, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            } else if (flag) {
                setupAsBought();
            }
        });



        btnAddNeededIngr.setOnClickListener(v -> {
            //TODO: Check what ingredients user has in his pantry and which & how much user need with the lines of the recipes of the weeklyplan
            flag = false;
            DB.calculateAndCreateToBuyList(DB.getCurrentUser(),currentToBuy.getId(),(success)->{
                if(success){
                    DB.reloadToBuyLines(DB.getCurrentUser(),currentToBuy.getId(),()->{
                        toBuyAdapter.updateList(DB.toBuyLinesArrayList);
                        toBuyAdapter.notifyDataSetChanged();
                        flag = true;
                    });
                }
            });
        });

        toBuyIngAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectIngrActivity.class);
            customLauncher.launch(intent);
        });
    }

    private void setupAsBought() {
        isBought = true;
        btnAddNeededIngr.setVisibility(View.INVISIBLE);
        btnDone.setText(R.string.next);
        ArrayList<ToBuyLine> lines = new ArrayList<>();
        for (int i = 0; i < DB.toBuyLinesArrayList.size() && i<toBuyAdapter.selected.length; i++) {
            if (toBuyAdapter.selected[i]) {
                lines.add(DB.toBuyLinesArrayList.get(i));
            }
        }
        setupRecyclerViewBought(lines);
        tvHint.setText(R.string.hintWhenBought);
    }

    private void setupRecyclerView() {
        toBuyAdapter = new ToBuyAdapter(this, DB.toBuyLinesArrayList);
        rvToBuy.setLayoutManager(new LinearLayoutManager(this));
        rvToBuy.setAdapter(toBuyAdapter);
    }

    private void setupRecyclerViewBought(ArrayList<ToBuyLine> lines) {
        boughtAdapter = new BoughtAdapter(this, lines);
        rvToBuy.setLayoutManager(new LinearLayoutManager(this));
        rvToBuy.setAdapter(boughtAdapter);
    }

    private final int ELIMINAR = 300;

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        posItem = item.getGroupId();
        int id = item.getItemId();
        switch (id) {
            case ELIMINAR:
                ToBuyLine toBuyLineToRemove = DB.toBuyLinesArrayList.get(posItem);
                DB.deleteToBuyLine(DB.currentUser, toBuyLineToRemove.getToBuyId(), toBuyLineToRemove.getId(), success -> {
                    if (success) {
                        toBuyAdapter.updateList(DB.toBuyLinesArrayList);
                        toBuyAdapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Failed to delete ToBuyLine with ID: " + toBuyLineToRemove.getId());
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }

}
