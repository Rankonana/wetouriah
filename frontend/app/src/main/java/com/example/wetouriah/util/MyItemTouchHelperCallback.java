package com.example.wetouriah.util;

import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wetouriah.PickUpRequestItem;
import com.example.wetouriah.adapters.listIteneraryAdapter;
import com.example.wetouriah.viewholders.listIteneraryViewHolder;

import java.util.Collections;
import java.util.List;

public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private int draggingPosition = -1;

    private Context context;
    private RecyclerView recyclerView;
    private listIteneraryAdapter adapter;
    private List<PickUpRequestItem> pickUpRequestItems;

    private final listIteneraryAdapter.PickUpRequestItemReorderListener pickUpRequestItemReorderListener;



    // Constructor if needed
    public MyItemTouchHelperCallback(RecyclerView recyclerView, listIteneraryAdapter adapter, List<PickUpRequestItem> pickUpRequestItems, listIteneraryAdapter.PickUpRequestItemReorderListener pickUpRequestItemReorderListener) {

        this.recyclerView =recyclerView;
        this.adapter = adapter;
        this.pickUpRequestItems = pickUpRequestItems;
        this.pickUpRequestItemReorderListener = pickUpRequestItemReorderListener;

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source,
                          RecyclerView.ViewHolder target) {
        int fromPosition = source.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        Collections.swap(pickUpRequestItems, fromPosition, toPosition);
        adapter.notifyItemMoved(fromPosition, toPosition);


        ((listIteneraryViewHolder) source).updatePosition(toPosition);
        ((listIteneraryViewHolder) target).updatePosition(fromPosition);


        pickUpRequestItemReorderListener.PickUpRequestItemReordered(pickUpRequestItems);



        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // Handle swipe actions if needed
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            // Item is being dragged, change its background color
            viewHolder.itemView.setBackgroundColor(Color.parseColor("#EFF1FF"));
            draggingPosition = viewHolder.getAdapterPosition();
        }
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (draggingPosition != -1) {
            // Item is no longer being dragged, reset its background color
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            draggingPosition = -1;
        }
    }
}
