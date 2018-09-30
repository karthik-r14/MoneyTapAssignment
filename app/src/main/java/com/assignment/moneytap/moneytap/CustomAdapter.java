package com.assignment.moneytap.moneytap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class CustomAdapter extends ArrayAdapter {
    public static final String IMAGE_NOT_FOUND_URL = "https://upload.wikimedia.org/wikipedia/commons/2/26/512pxIcon-sunset_photo_not_found.png";
    private ArrayList<Person> personsList;

    public CustomAdapter(@NonNull Context context, ArrayList<Person> personsList) {
        super(context, R.layout.custom_list_row, R.id.title, personsList);
        this.personsList = personsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_list_row, parent, false);

        ImageView personImage = row.findViewById(R.id.image);
        TextView title = row.findViewById(R.id.title);
        TextView description = row.findViewById(R.id.description);

        String imageUrl = personsList.get(position).getImageUrl();

        if (imageUrl == null) {
            Picasso.with(getContext())
                    .load(IMAGE_NOT_FOUND_URL)
                    .into(personImage);
        } else {
            Picasso.with(getContext())
                    .load(imageUrl)
                    .into(personImage);
        }

        title.setText(personsList.get(position).getTitle());
        description.setText(personsList.get(position).getDescription());

        return row;
    }
}
