package com.example.gf.seventhdemo.phonebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gf.seventhdemo.JavaBean.Linkman;
import com.example.gf.seventhdemo.R;

import java.util.ArrayList;
import java.util.List;

public class PhoneBookAdapter extends RecyclerView.Adapter<PhoneBookAdapter.MyViewHolder> {

    private List<Linkman> linkmanList;  //联系人列表
    private boolean ifShowCheckbox; //是否显示删除复选框
    private ArrayList<Linkman> delList; //待删除信息列表
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_itemView;
        LinearLayout ll_info;
        LinearLayout ll_call;
        LinearLayout ll_edit;
        TextView linkmanName;
        CheckBox delCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_itemView = (LinearLayout)itemView.findViewById(R.id.ll_phonebook_item);
            ll_info =(LinearLayout)itemView.findViewById(R.id.ll_phonebook_info);
            ll_call = (LinearLayout)itemView.findViewById(R.id.ll_call);
            ll_edit =(LinearLayout)itemView.findViewById(R.id.ll_phonebook_edit);
            linkmanName = (TextView) itemView.findViewById(R.id.tv_linkman_name);
            delCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox_phonebook_item);

        }
    }

    public PhoneBookAdapter(List<Linkman> linkmanList, Context context) {
        this.linkmanList = linkmanList;
        ifShowCheckbox = false;
        delList = new ArrayList<Linkman>();
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_phone_book_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        final Linkman linkman = linkmanList.get(position);
        final MyViewHolder viewHolder = myViewHolder;
        final int itemPosition = position;
        viewHolder.linkmanName.setText(linkman.getName());
        //设置删除复选框
        if (ifShowCheckbox) {
            viewHolder.delCheckBox.setVisibility(View.VISIBLE);
        } else {
            viewHolder.delCheckBox.setChecked(false);
            viewHolder.delCheckBox.setVisibility(View.GONE);
        }
        //设置删除复选框的事件
        viewHolder.delCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.delCheckBox.isChecked()) {
                    delList.add(linkman);
                } else {
                    delList.remove(linkman);
                }
            }
        });
        //显示详细信息
        viewHolder.ll_itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int visible = viewHolder.ll_info.getVisibility();
                if(visible == View.VISIBLE){
                    viewHolder.ll_info.setVisibility(View.GONE);
                }
                else{
                    viewHolder.ll_info.setVisibility(View.VISIBLE);
                }
            }
        });
        //拨打电话
        viewHolder.ll_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Linkman linkman = linkmanList.get(itemPosition);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"  + linkman.getPhoneNumber()));
                context.startActivity(intent);
            }
        });
        //启动编辑
        viewHolder.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Linkman linkman = linkmanList.get(position);
                PhoneBookEditActivity.actionStart(context,linkman.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return linkmanList.size();
    }

    public void showCheckbox(boolean ifShow) {
        this.ifShowCheckbox = ifShow;
        notifyDataSetChanged();
    }

    public ArrayList<Linkman> getDelList() {
        return delList;
    }
}
