package com.hafizzle.moneyspent.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hafizzle.moneyspent.Objects.UserEntry;
import com.hafizzle.moneyspent.R;
import com.hafizzle.moneyspent.SQLite.DBHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private static final String TAG = "RecyclerAdapter";

    private Context mContext;
    private ArrayList<UserEntry> mUserEntries;

    public RecyclerAdapter(Context mContext, ArrayList<UserEntry> mUserEntries) {
        this.mContext = mContext;
        this.mUserEntries = mUserEntries;
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        UserEntry userEntry = mUserEntries.get(position);
        holder.dateStampTextView.setText(userEntry.getDateStamp());
        holder.amountTextView.setText("$" + String.valueOf(userEntry.getMoneyAmount()));
        holder.cardViewItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                EditText input;
                android.support.v7.app.AlertDialog.Builder builder;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new android.support.v7.app.AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                }
                builder.setTitle("Delete Entry?")
                        .setMessage("")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DBHandler dbHandler = new DBHandler(mContext);
                                dbHandler.deleteRow(position);
                                mUserEntries.remove(position);
                                notifyItemRemoved(position);
                                    }
                                }).show();
                return true;
                            }
                        });
            }


    @Override
    public int getItemCount() {
        return mUserEntries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardViewItem;
        TextView dateStampTextView;
        TextView amountTextView;
        public ViewHolder(View itemView) {

            super(itemView);
            cardViewItem = itemView.findViewById(R.id.cardViewItem);
            dateStampTextView = itemView.findViewById(R.id.dateStampTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
        }
    }



}