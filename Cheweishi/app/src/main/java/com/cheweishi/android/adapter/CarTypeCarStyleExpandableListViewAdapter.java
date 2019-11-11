package com.cheweishi.android.adapter;

import java.util.List;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.QueryCarModeResponse;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class CarTypeCarStyleExpandableListViewAdapter extends
        BaseExpandableListAdapter {

    private Context context;
    private List<QueryCarModeResponse.MsgBean> list;
    private QueryCarModeResponse.MsgBean carType;

    public CarTypeCarStyleExpandableListViewAdapter(Context context,
                                                    List<QueryCarModeResponse.MsgBean> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list == null ? 0 : 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Long.parseLong(String.valueOf(list.get(childPosition).getId()));
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(
                R.layout.cartype_carstyle_group_listitem, null);
        TextView groupName = (TextView) convertView
                .findViewById(R.id.tv_groupname);
        groupName.setText(list.get(groupPosition).getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        Log.i("zhfy", "getChildView  groupPosition = " + groupPosition
                + " childPosition = " + childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.cartype_carstyle_child_listitem, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.gearbox = (TextView) convertView
                    .findViewById(R.id.tv_gearbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        carType = list.get(childPosition);
        holder.name.setText(carType.getName());
        // TODO 暂时去掉  2016-3-27 16:35:55
//        holder.gearbox.setText(carType.getGearbox());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private ViewHolder holder;

    class ViewHolder {

        private TextView name;
        private TextView gearbox;
    }
}
