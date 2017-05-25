package com.lyl.sharescontrol.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyl.sharescontrol.MyApp;
import com.lyl.sharescontrol.R;
import com.lyl.sharescontrol.net.entry.ShareEntry;

import java.util.ArrayList;

/**
 * Created by lyl on 2017/5/15.
 */

public class SharePriceAdapter extends RecyclerView.Adapter<SharePriceAdapter.MyShareViewHolder> {

    private Context mContext;
    private ArrayList<ShareEntry> mData;

    public SharePriceAdapter(Context context, ArrayList<ShareEntry> data) {
        mContext = context;
        mData = data;
    }

    public void setData(ArrayList<ShareEntry> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @Override
    public MyShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main, parent, false);
        return new MyShareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyShareViewHolder holder, final int position) {
        final ShareEntry share = mData.get(position);

        holder.name.setText(share.getName());
        holder.code.setText(share.getCode());
        holder.nowPrice.setText(share.getNowPrice() + "");
        holder.buyPrice.setText(share.getBuyPrice());
        holder.tallPrice.setText(share.getShareTall());
        holder.lowPrice.setText(share.getShareLow());

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)//
                        .setTitle("提示")//
                        .setMessage("您要删除该条数据吗？")//
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 修改数据库的数据
                                ShareEntry entry = share;
                                entry.setDelete(true);
                                MyApp.liteOrm.update(entry);

                                // 修改列表里的数据
                                mData.remove(position);

                                // 刷新页面
                                notifyDataSetChanged();
                                dialogInterface.dismiss();
                            }
                        })//
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null && mData.size() >= 0 ? mData.size() : 0;
    }

    class MyShareViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layout;
        TextView name;
        TextView code;
        TextView nowPrice;
        TextView buyPrice;
        TextView tallPrice;
        TextView lowPrice;

        public MyShareViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout);
            name = (TextView) view.findViewById(R.id.item_name);
            code = (TextView) view.findViewById(R.id.item_code);
            nowPrice = (TextView) view.findViewById(R.id.item_now_price);
            buyPrice = (TextView) view.findViewById(R.id.item_buy_price);
            tallPrice = (TextView) view.findViewById(R.id.item_tall_price);
            lowPrice = (TextView) view.findViewById(R.id.item_low_price);
        }
    }
}
