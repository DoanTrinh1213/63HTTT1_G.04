package space.app.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final String[] mItems;

    public CustomSpinnerAdapter(Context context, String[] items) {
        super(context, android.R.layout.simple_spinner_item, items);
        this.mContext = context;
        this.mItems = items;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(mItems[position]);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.parseColor("#FFC0CB"));

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(mItems[position]);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.parseColor("#FFC0CB"));

        return view;
    }
}
