package com.example.wetouriah.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wetouriah.AddCar;
import com.example.wetouriah.CustomerPortal;
import com.example.wetouriah.DriverPortal;
import com.example.wetouriah.PickUpRequestDetailed;
import com.example.wetouriah.PickUpRequestItem;
import com.example.wetouriah.R;
import com.example.wetouriah.viewholders.listIteneraryViewHolder;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;




public class listIteneraryAdapter extends RecyclerView.Adapter<listIteneraryViewHolder> {
    private Context context;
    private List<PickUpRequestItem> pickUpRequestItems;
    private List<PickUpRequestItem> originalPickUpRequestItems;




    public List<PickUpRequestItem> getItems() {
        return pickUpRequestItems;
    }
    public List<PickUpRequestItem> getOriginal() {
        return originalPickUpRequestItems;
    }

    public void setItems(List<PickUpRequestItem> items) {
        this.pickUpRequestItems = items;
        if( this.originalPickUpRequestItems==null){
            this.originalPickUpRequestItems = items;
        }
        notifyDataSetChanged();
    }


    public listIteneraryAdapter(Context context,  PickUpRequestItemReorderListener pickUpRequestItemReorderListener) {
        this.context = context;
//        this.pickUpRequestItems = pickUpRequestItems;
    }

    @Override
    public listIteneraryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itinerary_list_item, parent, false);
        return new listIteneraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(listIteneraryViewHolder holder, int position) {
        PickUpRequestItem routeItem = pickUpRequestItems.get(position);

//        holder.drag_indicator = itemView.findViewById(R.id.drag_indicator);
//        holder.status_image_view = itemView.findViewById(R.id.status_image_view);

        String positionText = String.valueOf(holder.getAdapterPosition() + 1);


        holder.position_text_view.setText(String.valueOf(positionText));


        String endlocation ;

        if(routeItem.getStatus().equals("5") || routeItem.getStatus().equals("8") || routeItem.getStatus().equals("1")){
            holder.position_text_view.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_light_red));
            String pickupLocation = routeItem.getPickup_location();

            if (pickupLocation.contains(",")) {
                // If the dropoffLocation contains a comma, split it and take the part before the comma
                String[] parts = pickupLocation.split(",");
                String truncatedText = parts[0].trim(); // Take the part before the comma and remove leading/trailing spaces
                if (truncatedText.length() > 30) {
                    truncatedText = truncatedText.substring(0, 30) + "…"; // Truncate and add ellipsis
                }
                holder.titleTextView.setText(truncatedText);
            } else if (pickupLocation.length() > 30) {
                // If there is no comma and the string is longer than 30 characters, truncate and add ellipsis
                String truncatedText = pickupLocation.substring(0, 30) + "…";
                holder.titleTextView.setText(truncatedText);
            } else {
                // If the string is 30 characters or shorter and does not contain a comma, display it as is
                holder.titleTextView.setText(pickupLocation);
            }

            holder.full_location.setText("Location: " + routeItem.getPickup_location());
            endlocation = routeItem.getPickup_location();

        }
        else {
            holder.position_text_view.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_circle_light_green));
            String dropoffLocation = routeItem.getDropoff_location();
            if (dropoffLocation.contains(",")) {
                // If the dropoffLocation contains a comma, split it and take the part before the comma
                String[] parts = dropoffLocation.split(",");
                String truncatedText = parts[0].trim(); // Take the part before the comma and remove leading/trailing spaces
                if (truncatedText.length() > 30) {
                    truncatedText = truncatedText.substring(0, 30) + "…"; // Truncate and add ellipsis
                }
                holder.titleTextView.setText(truncatedText);
            } else if (dropoffLocation.length() > 30) {
                // If there is no comma and the string is longer than 30 characters, truncate and add ellipsis
                String truncatedText = dropoffLocation.substring(0, 30) + "…";
                holder.titleTextView.setText(truncatedText);
            } else {
                // If the string is 30 characters or shorter and does not contain a comma, display it as is
                holder.titleTextView.setText(dropoffLocation);
            }
            holder.full_location.setText("Location: " + routeItem.getDropoff_location());

            endlocation = routeItem.getDropoff_location();

        }



        String status = routeItem.getStatus();

        if(status.equals("1")){
            status =  "Pending";
        }
        if(status.equals("2")){
            status =  "Picked-up";
        }
        if(status.equals("3")){
            status =  "Delivered to the Recipient";
        }
        if(status.equals("4")){
            status =  "In-transit";
        }
        if(status.equals("5")){
            status =  "Driver-assigned";
        }
        if(status.equals("6")){
            status =  "Out for delivery";
        }
        if(status.equals("7")){
            status =  "Return to Sender";
        }
        if(status.equals("8")){
            status =  "Out for pick-up";
        }
        if(status.equals("9")){
            status =  "Delivered at Warehouse";
        }
        if(status.equals("10")){
            status =  "Cancelled";
        }
        if(status.equals("11")){
            status =  "Collected by the Recipient";
        }
        if(status.equals("12")){
            status =  "Returned to Sender";
        }
        if(status.equals("13")){
            status =  "Delivery Attempt";
        }
        if(status.equals("14")){
            status =  "Failed Delivery";
        }
        if(status.equals("15")){
            status =  "Lost / Stolen";
        }
        if(status.equals("16")){
            status =  "Out for Delivery";
        }







        holder.status.setText("Status: "+ status);
        holder.tracking_number.setText("Tracking Number: "+ routeItem.getTracking_number());


        holder.expand_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.expandable_view.getVisibility() == View.VISIBLE) {
                    holder.expandable_view.setVisibility(View.GONE);

//                    holder.expand_button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_baseline_expand_less_24, 0, 0);

                } else {
                    holder.expandable_view.setVisibility(View.VISIBLE);

//                    holder.expand_button.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_expand_more_arrow, 0, 0);

                }
            }
        });


        holder.details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PickUpRequestDetailed.class);
                intent.putExtra("model", routeItem);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag
                context.startActivity(intent);
            }
        });

        holder.navigate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGoogleMapsDirections(context,endlocation);

            }
        });
        holder.complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(holder.getAdapterPosition());
            }
        });

    }

    public void removeItem(int position) {
        pickUpRequestItems.remove(position);
        notifyItemRemoved(position);
    }




    public interface PickUpRequestItemReorderListener {
        void PickUpRequestItemReordered(List<PickUpRequestItem> routeItem);
    }

    public static void openGoogleMapsDirections(Context context, String destinationAddress) {
        double originLat, originLng, destLat, destLng;

        // Geocode origin address to coordinates if needed
//        if (TextUtils.isEmpty(originAddress)) {
//            // Handle case when origin address is not provided
//            Toast.makeText(context, "Origin address is missing", Toast.LENGTH_SHORT).show();
//            return;
//        } else {
//            LatLng originLatLng = geocodeAddress(context, originAddress);
//            if (originLatLng == null) {
//                // Handle case when origin address is not valid or couldn't be geocoded
//                Toast.makeText(context, "Invalid origin address", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            originLat = originLatLng.latitude;
//            originLng = originLatLng.longitude;
//        }

        // Geocode destination address to coordinates if needed
        if (TextUtils.isEmpty(destinationAddress)) {
            // Handle case when destination address is not provided
            Toast.makeText(context, "Destination address is missing", Toast.LENGTH_SHORT).show();
            return;
        } else {
            LatLng destLatLng = geocodeAddress(context, destinationAddress);
            if (destLatLng == null) {
                // Handle case when destination address is not valid or couldn't be geocoded
                Toast.makeText(context, "Invalid destination address", Toast.LENGTH_SHORT).show();
                return;
            }
            destLat = destLatLng.latitude;
            destLng = destLatLng.longitude;
        }

       // Uri uri = Uri.parse("google.navigation:q=" + destLat + "," + destLng);
        Uri uri = Uri.parse("google.navigation:q=" + destinationAddress);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag
        intent.setPackage("com.google.android.apps.maps");

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);


        } else {
            Toast.makeText(context, "Google Maps app is not installed", Toast.LENGTH_SHORT).show();
        }
    }
    private static LatLng geocodeAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocationName(address, 1);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if (addresses != null && addresses.size() > 0) {
            Address location = addresses.get(0);
            return new LatLng(location.getLatitude(), location.getLongitude());
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return pickUpRequestItems.size();
    }


}
