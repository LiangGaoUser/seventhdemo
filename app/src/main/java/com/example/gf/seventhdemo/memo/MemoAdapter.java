package com.example.gf.seventhdemo.memo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.gf.seventhdemo.JavaBean.Memo;
import com.example.gf.seventhdemo.R;

import java.util.ArrayList;
import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MyViewHolder> {

    private List<Memo> memoList;  //记事本列表
    private ArrayList<Memo> delList; //待删除信息列表
    private boolean ifShowCheckbox; //是否显示删除复选框
    private Context context;

    // 内部类：用于描述RecyclerView子项的布局
    class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView;   //子项布局
        TextView memoTitle;
        TextView memoContent;
        TextView memoCreateTime;
        CheckBox delCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            memoTitle = (TextView) itemView.findViewById(R.id.tv_memo_title);
            memoContent = (TextView) itemView.findViewById(R.id.tv_memo_content);
            memoCreateTime = (TextView) itemView.findViewById(R.id.tv_memo_createtime);
            delCheckBox = (CheckBox) itemView.findViewById(R.id.checkBox_memo_item);
        }
    }

    // 构造方法
    public MemoAdapter(List<Memo> memoList, Context context) {
        this.memoList = memoList;
        ifShowCheckbox = false;
        delList = new ArrayList<Memo>();
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_memo_item, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        //为标签布局添加点击事件
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                Memo aMemo = memoList.get(position);
                MemoEditActivity.actionStart(context,aMemo.getId(),aMemo.getTitle(),aMemo.getContent(),aMemo.getCreateTime());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        // 通过参数 position 获得 RecyclerView 当前子项的实例
        final Memo aMemo = memoList.get(position);
        final MyViewHolder viewHolder = myViewHolder;
        //如果memo的内容过长，则仅显示前20个字符
        String content = aMemo.getContent();
        content = content.replace("\n", " ");
        if (content.length() >= 20) {
            content = content.substring(0, 20);
            content = content + "...";
        }
        viewHolder.memoTitle.setText(aMemo.getTitle());
        viewHolder.memoContent.setText(content);
        viewHolder.memoCreateTime.setText(aMemo.getCreateTime());
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
                    delList.add(aMemo);
                } else {
                    delList.remove(aMemo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

    public void showCheckbox(boolean ifShow) {
        this.ifShowCheckbox = ifShow;
        notifyDataSetChanged();
    }

    public ArrayList<Memo> getDelList() {
        return delList;
    }
}
