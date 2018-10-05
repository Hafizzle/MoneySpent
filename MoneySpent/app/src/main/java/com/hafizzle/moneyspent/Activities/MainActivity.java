package com.hafizzle.moneyspent.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.hafizzle.moneyspent.Objects.UserEntry;
import com.hafizzle.moneyspent.R;
import com.hafizzle.moneyspent.SQLite.DBHandler;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private TickerView mTicker;
    private Handler handler;
    public EditText mInput;
    FloatingActionButton addFAB;
    DBHandler dbHandler;
    public String tempInput = "";
    public double d = 0;
    ArrayList<UserEntry> userEntries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: ");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        dbHandler = new DBHandler(this);
        d = retrieveMoneyTotal();
        handler = new Handler();
        mTicker = findViewById(R.id.tickerView);
        mTicker.setCharacterLists(TickerUtils.provideNumberList());
        addFAB = (FloatingActionButton) findViewById(R.id.addFAB);
        mTicker.setAnimationDuration(500);
        mTicker.setTextColor(Color.parseColor("#FF0000"));
        mTicker.setText("$" + Double.toString(d));

    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        Runnable retrieveRun = new Runnable() {
            @Override
            public void run() {
                d = retrieveMoneyTotal();
            }
        };

        Thread retrieveThread = new Thread(retrieveRun);
        retrieveThread.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTicker.setText("$" + Double.toString(d));
            }
        }, 700);

        super.onResume();
    }

    public void openSpendingActivity(View v) {
        openSpendingActivity();
    }

    public void addAmount(View v) {//This is the onclick for the addFAB
        android.support.v7.app.AlertDialog.Builder builder;
        mInput = new EditText(this);
        mInput.setGravity(Gravity.CENTER_HORIZONTAL);
        mInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        mInput.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mInput.addTextChangedListener(new TextWatcher() {
            DecimalFormat dec = new DecimalFormat("0.00");

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            private String current = "";
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(current)){
                    mInput.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[$,.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                    current = formatted;
                    mInput.setText(formatted);
                    mInput.setSelection(formatted.length());

                    mInput.addTextChangedListener(this);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.support.v7.app.AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(this);
        }
        builder.setTitle("How much did you spend?")
                .setMessage("")
                .setView(mInput)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Log.d(TAG, "onClick: added");
                        addMoneyTotal(mInput);

                        Runnable retrieveRun = new Runnable() {
                            @Override
                            public void run() {
                                d = retrieveMoneyTotal();
                            }
                        };

                        Thread retrieveThread = new Thread(retrieveRun);
                        retrieveThread.start();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTicker.setText("$" + Double.toString(d));
                            }
                        }, 100);


                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void addMoneyTotal(EditText input) {
        if (input.length() != 0) {
            userEntries = dbHandler.getData();
            tempInput = input.getText().toString().substring(1);
            double n = Double.parseDouble(tempInput);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDateFormat.format(new Date());
            dbHandler.addData(new UserEntry(date, n), userEntries);
        }
        else {
            Toast.makeText(this, "No amount added!", Toast.LENGTH_SHORT).show();
        }
    }

    private double retrieveMoneyTotal() {
        Log.d(TAG, "Retrieving all data from column 1 and sum");
        Double total = dbHandler.getTotal();
        return total;
    }

    public void openSpendingActivity() {
        Intent intent = new Intent(this, SpendingDisplayActivity.class);
        startActivity(intent);
    }
}


