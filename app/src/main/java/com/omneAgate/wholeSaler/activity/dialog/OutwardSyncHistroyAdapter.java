package com.omneAgate.wholeSaler.activity.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 24/8/16.
 */
public class OutwardSyncHistroyAdapter extends BaseAdapter {

    Context ct;

    List<WholesalerPostingDto> outwardhistroydto;

    public OutwardSyncHistroyAdapter(Context context,List<WholesalerPostingDto> outwardhistroydto){
         this.ct=context;
         this.outwardhistroydto=outwardhistroydto;
    }
    @Override
    public int getCount() {
        return outwardhistroydto.size();
    }

    @Override
    public Object getItem(int position) {
        return outwardhistroydto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = null;
        WholesalerPostingDto outwards = outwardhistroydto.get(position);
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) ct
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.adapter_kerosene_stock_outward, null);
            holder = new ViewHolder();
            holder.TransactionNumber = (TextView) view.findViewById(R.id.tvTransactionNumber);
            holder.code = (TextView) view.findViewById(R.id.tvRecipientCode);
            holder.outwardDate = (TextView) view.findViewById(R.id.tvOutwardDate);
            holder.tv_quantity = (TextView) view.findViewById(R.id.tvQuantity);

            view.setMinimumHeight(50);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }

        NumberFormat format = new DecimalFormat("#0.000");

        holder.TransactionNumber.setText(outwards.getReferenceNo());
        holder.outwardDate.setText(outwards.getOutwardDate());
        holder.tv_quantity.setText(""+format.format(outwards.getQuantity()));

        String recipientType=outwards.getRecipientType();
        if(GlobalAppState.language.equalsIgnoreCase("ta")){
            if(recipientType.equalsIgnoreCase("Kerosene Bunk")){
                recipientType="மண்ணெண்ணெய் கடை";
            }else if(recipientType.equalsIgnoreCase("FPS")){
                recipientType=" நியாய விலைக் கடை";
            }else {
                recipientType=" ஆர்ஆர்சி";
            }
        }
        holder.code.setText(recipientType+" / "+outwards.getCode());


        return view;
    }

    class ViewHolder {
        TextView TransactionNumber;
        TextView code;
        TextView outwardDate;
        TextView tv_quantity;

    }
}
