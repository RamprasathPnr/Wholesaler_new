package com.omneAgate.wholeSaler.activity.dialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.omneAgate.wholeSaler.DTO.Wholesaler_allData;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;

import java.util.List;

/**
 * Created by root on 26/8/16.
 */
public class AssociatedCardAdapter extends BaseAdapter {
    Context context;

    List<Wholesaler_allData> allData;

    public AssociatedCardAdapter(Context context,List<Wholesaler_allData> alldata){
        this.context=context;
        this.allData=alldata;
        Log.e("Associatedcard size",""+alldata.size());

    }
    @Override
    public int getCount() {
        return allData.size();
    }

    @Override
    public Object getItem(int position) {
        return allData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            View view = null;


            Wholesaler_allData wholesalerAllData = allData.get(position);
        try {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.adpter_typelist, null);
                holder = new ViewHolder();
                holder.ShopType = (TextView) view.findViewById(R.id.tv_shoptype);
                holder.ShopCode = (TextView) view.findViewById(R.id.tv_shopcode);
                holder.ShopName = (TextView) view.findViewById(R.id.tv_shopname);
                holder.Contact_personName = (TextView) view.findViewById(R.id.tv_contactname);
                holder.Contact_personNumber = (TextView) view.findViewById(R.id.tv_contactnumber);

                view.setMinimumHeight(50);
                view.setTag(holder);

            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }
            String ShopType = wholesalerAllData.getShop_type();

            if (GlobalAppState.language.equalsIgnoreCase("ta")) {
                if (ShopType.equalsIgnoreCase("Kerosene Bunk")) {
                    ShopType = "மண்ணெண்ணெய் கடை";
                } else if (ShopType.equalsIgnoreCase("FPS")) {
                    ShopType = " நியாய விலைக் கடை";
                } else {
                    ShopType = " ஆர்ஆர்சி";
                }
            }
            holder.ShopType.setText("" + ShopType);
            holder.ShopCode.setText("" + wholesalerAllData.getShop_code());
            holder.ShopName.setText("" + wholesalerAllData.getShop_name());
            holder.Contact_personName.setText("" + wholesalerAllData.getContact_persion());
            holder.Contact_personNumber.setText("" + wholesalerAllData.getContact_number());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Associated card", "error occured " + e.toString());
        }

        return view;
    }


    class ViewHolder {
        TextView ShopType;
        TextView ShopCode;
        TextView ShopName;
        TextView Contact_personName;
        TextView Contact_personNumber;

    }
}
