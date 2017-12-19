package com.omneAgate.wholeSaler.activity.dialog;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.omneAgate.wholeSaler.DTO.WholesalerPostingDto;
import com.omneAgate.wholeSaler.activity.GlobalAppState;
import com.omneAgate.wholeSaler.activity.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by root on 29/8/16.
 */
public class outWardHistroyAdapter extends BaseAdapter {

    private Context context;

    private List<WholesalerPostingDto> keroseneStockOutwardList;


    public outWardHistroyAdapter(Context context ,List<WholesalerPostingDto> keroseneStockOutwardList){
        this.context=context;
        this.keroseneStockOutwardList=keroseneStockOutwardList;
        Log.e("outwardHistroyAdapter","size of the list"+keroseneStockOutwardList.size());

    }
    @Override
    public int getCount() {
        return keroseneStockOutwardList.size();
    }

    @Override
    public Object getItem(int position) {
        return keroseneStockOutwardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view=null;

        WholesalerPostingDto wholesalerpostingdata=keroseneStockOutwardList.get(position);
        try{
            if(convertView ==null){

                LayoutInflater mInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = mInflater.inflate(R.layout.adapter_kerosene_stock_outward, null);
                holder = new ViewHolder();

                holder.linearLayout = (LinearLayout) view.findViewById(R.id.linearLayoutAdapter);
                holder.transactionNo = (TextView) view.findViewById(R.id.tvTransactionNumber);
                holder.recipientcode = (TextView) view.findViewById(R.id.tvRecipientCode);
                holder.outwardDate = (TextView) view.findViewById(R.id.tvOutwardDate);
                holder.quantity = (TextView) view.findViewById(R.id.tvQuantity);

                //     view.setMinimumHeight(50);
                view.setTag(holder);

            } else {
                view = convertView;
                holder = (ViewHolder) convertView.getTag();
            }

            NumberFormat format = new DecimalFormat("#0.000");
            holder.transactionNo.setText(wholesalerpostingdata.getReferenceNo());

            holder.outwardDate.setText(wholesalerpostingdata.getOutwardDate());
            String recipientType=wholesalerpostingdata.getRecipientType();
            if(GlobalAppState.language.equalsIgnoreCase("ta")){
                if(recipientType.equalsIgnoreCase("Kerosene Bunk")){
                    recipientType="மண்ணெண்ணெய் கடை";
                }else if(recipientType.equalsIgnoreCase("FPS")){
                    recipientType=" நியாய விலைக் கடை";
                }else {
                    recipientType=" ஆர்ஆர்சி";
                }
            }

            holder.recipientcode.setText(recipientType+" / "+wholesalerpostingdata.getCode());
            holder.quantity.setText(""+format.format(wholesalerpostingdata.getQuantity()));
            Log.e("OutwardHistroy","get status "+wholesalerpostingdata.getStatus());
            if(wholesalerpostingdata.getStatus().equals("R")){
                view.setBackgroundColor(Color.parseColor("#fce5e8"));
            }else{
                view.setBackgroundColor(Color.parseColor("#ffffff"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    class ViewHolder{
        TextView transactionNo;
        TextView recipientcode;
        TextView outwardDate;
        TextView quantity;
        LinearLayout linearLayout;
    }
}

