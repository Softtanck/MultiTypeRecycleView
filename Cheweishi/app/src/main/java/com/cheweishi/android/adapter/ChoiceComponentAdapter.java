package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.ComponentServiceResponse;

/**
 * Created by tangce on 6/2/2016.
 */
public class ChoiceComponentAdapter extends BaseExpandableListAdapter {

    private Context context;

    private ComponentServiceResponse response;

    public ChoiceComponentAdapter(Context context, ComponentServiceResponse response) {
        this.context = context;
        this.response = response;
    }


    public void setData(ComponentServiceResponse response) {
        if (null != response) {
            this.response = response;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getGroupCount() {
        return response.getMsg().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return response.getMsg().get(groupPosition).getItemParts().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return response.getMsg().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return response.getMsg().get(groupPosition).getItemParts().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewGroupHolder holder;
        if (null == convertView) {
            holder = new ViewGroupHolder();
            convertView = View.inflate(context, R.layout.item_choice_group, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_item_choice_group_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewGroupHolder) convertView.getTag();
        }

        holder.textView.setText(response.getMsg().get(groupPosition).getServiceItemName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewChildHolder holder;
        if (null == convertView) {
            holder = new ViewChildHolder();
            convertView = View.inflate(context, R.layout.item_choice_child, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_choice_child_name);
            holder.price = (TextView) convertView.findViewById(R.id.tv_choice_child_price);
            holder.rl_choice_child = (RelativeLayout) convertView.findViewById(R.id.rl_choice_child);
            convertView.setTag(holder);
        } else {
            holder = (ViewChildHolder) convertView.getTag();
        }
        holder.name.setText(response.getMsg().get(groupPosition).getItemParts().get(childPosition).getServiceItemPartName());
        holder.price.setText("￥" + response.getMsg().get(groupPosition).getItemParts().get(childPosition).getPrice() + "元");

        if (response.getMsg().get(groupPosition).getItemParts().get(childPosition).isIsDefault()) {
            holder.rl_choice_child.setBackgroundResource(R.drawable.pay_money_false);
            holder.name.setTextColor(context.getResources().getColor(R.color.orange));
            holder.price.setTextColor(context.getResources().getColor(R.color.orange));
        } else {
            holder.rl_choice_child.setBackgroundResource(R.color.white_alpha);
            holder.name.setTextColor(context.getResources().getColor(R.color.hyal));
            holder.price.setTextColor(context.getResources().getColor(R.color.hyal));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewGroupHolder {
        TextView textView;
    }

    private class ViewChildHolder {
        TextView name;
        TextView price;
        RelativeLayout rl_choice_child;
    }
}
