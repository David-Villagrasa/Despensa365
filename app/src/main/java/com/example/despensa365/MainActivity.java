package com.example.despensa365;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.despensa365.activities.LoginActivity;
import com.example.despensa365.activities.PantryActivity;
import com.example.despensa365.activities.RecipeActivity;
import com.example.despensa365.activities.RegisterActivity;
import com.example.despensa365.activities.RegisterPantryActivity;
import com.example.despensa365.activities.ToBuyActivity;
import com.example.despensa365.activities.WeekActivity;
import com.example.despensa365.db.DB;
import com.example.despensa365.dialogs.ToBuyTitleDialogFragment;
import com.example.despensa365.objects.ToBuy;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ToBuyTitleDialogFragment.ToBuyTitleDialogListener {
    public FirebaseAuth firebaseAuth;
    Button btnLogin, btnRegister, btnLogout, btnManWeek, btnManRec, btnManPantry, btnManToBuy;
    TextView tvWelcome;
    ActivityResultLauncher<Intent> customLauncher;
    ActivityResultLauncher<Intent> pantryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB.init();

        btnLogin = findViewById(R.id.btnLogIn);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogout = findViewById(R.id.btnLogOut);
        btnManWeek = findViewById(R.id.btnManageWeek);
        btnManRec = findViewById(R.id.btnManageRecipes);
        btnManPantry = findViewById(R.id.btnManagePantry);
        btnManToBuy = findViewById(R.id.btnManageList);
        tvWelcome = findViewById(R.id.tvWelcome);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            defaultLayout();
        } else {
            loggedLayout();
        }
        customLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (firebaseAuth.getCurrentUser() == null) {
                defaultLayout();
            } else {
                loggedLayout();
            }
        });
        pantryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Toast.makeText(this, R.string.createdSuccessfullyPantry, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PantryActivity.class);
                customLauncher.launch(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_language) {
            showChangeLanguageDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showChangeLanguageDialog() {
        final String[] languages = {"Español", "English"};
        new AlertDialog.Builder(this)
                .setTitle(R.string.changeLanguage)
                .setItems(languages, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            setLocale("es");
                            break;
                        case 1:
                            setLocale("en");
                            break;
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create()
                .show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    private void defaultLayout() {
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.VISIBLE);
        btnLogout.setVisibility(View.INVISIBLE);
        tvWelcome.setVisibility(View.INVISIBLE);
    }

    private void loggedLayout() {
        btnLogin.setVisibility(View.INVISIBLE);
        btnRegister.setVisibility(View.INVISIBLE);
        btnLogout.setVisibility(View.VISIBLE);
        tvWelcome.setVisibility(View.VISIBLE);
        DB.currentUser = firebaseAuth.getCurrentUser();
        String username = DB.currentUser.getDisplayName();
        tvWelcome.setText(String.format("%s %s", getString(R.string.welcome), username));
        DB.setupDateWeekPlan(DB.currentUser);
        DB.getAllIngredients(DB.currentUser);
        DB.reloadRecipes(DB.currentUser);
        DB.reloadIngredients(DB.currentUser);
    }

    public void clickLogin(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        customLauncher.launch(intent);
    }

    public void clickRegister(View v) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        customLauncher.launch(intent);
    }

    public void clickLogout(View v) {
        firebaseAuth.signOut();
        defaultLayout();
    }

    public void clickWeek(View v) {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, WeekActivity.class);
            customLauncher.launch(intent);
        } else {
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickRecipes(View v) {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
            customLauncher.launch(intent);
        } else {
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickPantry(View v) {
        if (firebaseAuth.getCurrentUser() != null) {
            DB.checkPantryExists(DB.currentUser, exists -> {
                if (exists) {
                    Intent intent = new Intent(MainActivity.this, PantryActivity.class);
                    customLauncher.launch(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, RegisterPantryActivity.class);
                    pantryLauncher.launch(intent);
                }
            });
        } else {
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    public void clickToBuy(View v) {
        if (firebaseAuth.getCurrentUser() != null) {
            DB.checkToBuyExists(DB.currentUser, exists -> {
                if (!exists) {
                    showToBuyTitleDialog();
                } else {
                    Intent intent = new Intent(MainActivity.this, ToBuyActivity.class);
                    customLauncher.launch(intent);
                }
            });
        } else {
            Toast.makeText(this, R.string.hintMainActivity, Toast.LENGTH_SHORT).show();
        }
    }

    private void showToBuyTitleDialog() {
        ToBuyTitleDialogFragment dialog = new ToBuyTitleDialogFragment();
        dialog.setListener(this);
        dialog.show(getSupportFragmentManager(), "ToBuyTitleDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(String title) {
        if (!title.isEmpty()) {
            createNewToBuyList(title);
        } else {
            Toast.makeText(this, R.string.fillTitleToBuy, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogNegativeClick(ToBuyTitleDialogFragment dialog) {
        dialog.dismiss();
    }

    private void createNewToBuyList(String title) {
        ToBuy newToBuy = new ToBuy();
        newToBuy.setTitle(title);
        newToBuy.setUserId(DB.currentUser.getUid());

        DB.addToBuy(DB.currentUser, newToBuy, success -> {
            if (success) {
                Intent intent = new Intent(MainActivity.this, ToBuyActivity.class);
                customLauncher.launch(intent);
            } else {
                Toast.makeText(MainActivity.this, R.string.failToCreateToBuy, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
