package com.desiredsoftware.nurseapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterRecyclerViewNotifications extends RecyclerView.Adapter<AdapterRecyclerViewNotifications.NotificationViewHolder> {

    private ArrayList<RecyclerViewNotificationItem> itemsArray;

    public AdapterRecyclerViewNotifications(ArrayList<RecyclerViewNotificationItem> itemsArray) {
        this.itemsArray = itemsArray;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textView_notificationTime;
        private Switch isEnabled;
        //private String[] actionsList;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_notificationTime = itemView.findViewById(R.id.time_item);
            this.isEnabled = itemView.findViewById(R.id.switch_notification_is_enabled);
            //this.actionsList = actionsList;
        }
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NotificationViewHolder notificationViewHolder = null;
        try
        {
            View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
            notificationViewHolder = new NotificationViewHolder(view);
        }
        catch (Exception e)
        {
            String message = e.getMessage();
            Log.v("onCreateViewHolder", message);
        }


        return notificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        RecyclerViewNotificationItem recyclerItem = itemsArray.get(position);
        holder.textView_notificationTime.setText(recyclerItem.getNotificationTime());
        holder.isEnabled.setEnabled(recyclerItem.getIsEnabled());
    }

    @Override
    public int getItemCount()
    {
        return itemsArray.size();
    }
}
