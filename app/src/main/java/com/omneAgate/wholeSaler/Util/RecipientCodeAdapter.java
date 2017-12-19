package com.omneAgate.wholeSaler.Util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.omneAgate.wholeSaler.activity.R;

import java.util.ArrayList;
import java.util.Locale;

public class RecipientCodeAdapter extends ArrayAdapter<String> {
    private final String MY_DEBUG_TAG = "RecipentAdapter";
    private ArrayList<String> items;
    private ArrayList<String> itemsAll;
    private ArrayList<String> suggestions;
    private int viewResourceId;

    public RecipientCodeAdapter(Context context, int viewResourceId, ArrayList<String> items) {
        super(context, viewResourceId, items);
        this.items = items;

        this.itemsAll = (ArrayList<String>) items.clone();
        this.suggestions = new ArrayList<String>();
        this.viewResourceId = viewResourceId;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        String recipientCode = items.get(position);
        if (recipientCode != null) {
            TextView recipientCodeLabel = (TextView) v.findViewById(R.id.recipientCodeLabel);
            if (recipientCodeLabel != null) {
            // Log.e(MY_DEBUG_TAG, "getView Recipient Code:" + recipientCode);
                recipientCodeLabel.setText(recipientCode);
            }
        }
        return v;
    }




    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
          //  String str = ((String)(resultValue)).getName();
            String str = ((String)(resultValue));
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (String recipientCode : itemsAll) {
                    if (recipientCode.toLowerCase().toLowerCase(Locale.ENGLISH).contains(constraint.toString().toLowerCase())) {
                        suggestions.add(recipientCode);

                    }


                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<String> filteredList = (ArrayList<String>) results.values;

            if(results != null && results.count > 0) {
                clear();
                for (String c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }else{
                notifyDataSetInvalidated();
            }


        }
    };

}

