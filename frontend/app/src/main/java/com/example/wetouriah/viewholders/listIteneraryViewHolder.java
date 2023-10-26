package com.example.wetouriah.viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wetouriah.R;


public class listIteneraryViewHolder extends RecyclerView.ViewHolder {

    public final ImageView drag_indicator,status_image_view;
    public final TextView position_text_view,titleTextView,full_location,tracking_number,status;

    public final Button expand_button;
    public final  LinearLayout expandable_view;

    public final Button details_button,navigate_button,complete_button;

    public boolean isExpanded = false;
    private int position;

    public listIteneraryViewHolder(View itemView) {
        super(itemView);
        drag_indicator = itemView.findViewById(R.id.drag_indicator);
        status_image_view = itemView.findViewById(R.id.status_image_view);
        position_text_view = itemView.findViewById(R.id.position_text_view);
        titleTextView = itemView.findViewById(R.id.title_text_view);

        expand_button = itemView.findViewById(R.id.expand_button);

        expandable_view = itemView.findViewById(R.id.expandable_view);


        full_location = itemView.findViewById(R.id.full_location);
        tracking_number = itemView.findViewById(R.id.tracking_number);
        status = itemView.findViewById(R.id.status);






        details_button = itemView.findViewById(R.id.details_button);
        navigate_button = itemView.findViewById(R.id.navigate_button);
        complete_button = itemView.findViewById(R.id.complete_button);

    }
    public void updatePosition(int position) {
        this.position = position;
    }
    private void configurePositionTextView() {
        position_text_view.setText( String.valueOf(position));
    }



}
