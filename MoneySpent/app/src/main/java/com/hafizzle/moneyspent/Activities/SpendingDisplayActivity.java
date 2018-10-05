package com.hafizzle.moneyspent.Activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;

import com.hafizzle.moneyspent.Adapters.RecyclerAdapter;
import com.hafizzle.moneyspent.Adapters.SimpleDividerItemDecoration;
import com.hafizzle.moneyspent.Objects.UserEntry;
import com.hafizzle.moneyspent.R;
import com.hafizzle.moneyspent.SQLite.DBHandler;

import java.util.ArrayList;

public class SpendingDisplayActivity extends AppCompatActivity {
    ArrayList<UserEntry> mUserEntries;
    DBHandler dbHandler;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_display);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        emptyView = (TextView)findViewById(R.id.emptyView);
        dbHandler = new DBHandler(this);

        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this, (mUserEntries = dbHandler.getData()));
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        if (mUserEntries.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
